package com.example.shramsarthi.data.firebase

import android.util.Log
import com.example.shramsarthi.data.repository.UserRepository
import com.example.shramsarthi.model.User
import com.example.shramsarthi.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Robust Cloud Firestore implementation for user profile persistence.
 * Implements idempotent save strategies, cache fallback verification,
 * and comprehensive error mapping to avoid false timeout failures.
 */
class FirestoreUserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserRepository {

    companion object {
        private const val TAG = "FirestoreUserRepo"
        private const val READ_TIMEOUT_MS = 10000L
        private const val WRITE_TIMEOUT_MS = 15000L
        private const val VERIFY_TIMEOUT_MS = 4000L
    }

    private val usersCollection = firestore.collection("users")

    override suspend fun getUser(uid: String): Result<User?> = runCatching {
        val cleanUid = uid.trim()
        if (cleanUid.isBlank()) return@runCatching null

        Log.d(TAG, "getUser: Fetching profile for uid=$cleanUid")

        try {
            // Attempt standard network/cache get with reasonable timeout
            val snapshot = withTimeout(READ_TIMEOUT_MS) {
                usersCollection.document(cleanUid).get().awaitTask()
            }
            if (snapshot.exists()) {
                Log.d(TAG, "getUser: Profile found for uid=$cleanUid")
                snapshotToUser(snapshot.id, snapshot.data ?: emptyMap())
            } else {
                Log.d(TAG, "getUser: No document exists at users/$cleanUid")
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "getUser: Primary fetch failed (${e.javaClass.simpleName}: ${e.message}), checking local cache fallback...")

            // Check local cache directly in case of slow initial network handshake
            val cacheSnapshot = runCatching {
                usersCollection.document(cleanUid).get(Source.CACHE).awaitTask()
            }.getOrNull()

            if (cacheSnapshot != null && cacheSnapshot.exists()) {
                Log.d(TAG, "getUser: Resolved profile from local cache for uid=$cleanUid")
                snapshotToUser(cacheSnapshot.id, cacheSnapshot.data ?: emptyMap())
            } else {
                // If permission denied, surface immediately
                if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    throw ProfileException.PermissionDenied(e)
                }
                throw mapFirestoreException(e)
            }
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        val uid = user.uid.trim()
        if (uid.isBlank()) {
            return Result.failure(ProfileException.InvalidData("User UID cannot be empty when creating profile."))
        }

        // Validate authenticated Firebase UID
        val auth = FirebaseAuth.getInstance()
        val currentAuthUid = auth.currentUser?.uid
        if (currentAuthUid == null) {
            Log.e(TAG, "saveUser failed: FirebaseAuth current user is null")
            return Result.failure(ProfileException.Unauthenticated())
        }
        if (currentAuthUid != uid) {
            Log.e(TAG, "saveUser failed: Authenticated UID ($currentAuthUid) does not match target UID ($uid)")
            return Result.failure(ProfileException.PermissionDenied())
        }

        Log.d(TAG, "saveUser: Starting save for uid=$uid, role=${user.role.name}")

        // Step 1: Idempotent pre-check and preservation of existing createdAt
        var finalCreatedAt = user.createdAt
        try {
            val existingSnapshot = withTimeoutOrNull(VERIFY_TIMEOUT_MS) {
                usersCollection.document(uid).get().awaitTask()
            }
            if (existingSnapshot != null && existingSnapshot.exists()) {
                val data = existingSnapshot.data ?: emptyMap()
                val existingCreatedAt = when (val raw = data["createdAt"]) {
                    is Number -> raw.toLong()
                    is com.google.firebase.Timestamp -> raw.toDate().time
                    else -> null
                }
                if (existingCreatedAt != null && existingCreatedAt > 0) {
                    finalCreatedAt = existingCreatedAt
                }

                val existingUser = snapshotToUser(uid, data)
                if (existingUser.isProfileComplete() &&
                    existingUser.fullName == user.fullName &&
                    existingUser.role == user.role &&
                    existingUser.location == user.location &&
                    existingUser.primarySkill == user.primarySkill
                ) {
                    Log.i(TAG, "saveUser: Profile at users/$uid is already complete and identical. Idempotent success.")
                    return Result.success(Unit)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "saveUser: Pre-check get() encountered exception: ${e.message} (proceeding with save)")
        }

        // Step 2: Prepare payload ensuring createdAt is consistent and immutable
        val userToSave = user.copy(createdAt = finalCreatedAt)
        val data = userToMap(userToSave)

        // Step 3: Perform write with timeout
        return try {
            withTimeout(WRITE_TIMEOUT_MS) {
                usersCollection.document(uid).set(data, SetOptions.merge()).awaitTask()
            }
            Log.i(TAG, "saveUser: Successfully acknowledged by Firestore server for uid=$uid")
            Result.success(Unit)
        } catch (throwable: Throwable) {
            Log.w(TAG, "saveUser: Exception during write: ${throwable.javaClass.simpleName} - ${throwable.message}")

            // Step 4: Post-write verification check
            // If the write timed out or experienced transient network drop, check if the document
            // was committed to local cache or acknowledged by Firestore server.
            val isCommitted = verifyDocumentExists(uid)
            if (isCommitted) {
                Log.i(TAG, "saveUser: Post-write verification succeeded for uid=$uid. Profile is intact in Firestore.")
                return Result.success(Unit)
            }

            val mapped = mapFirestoreException(throwable)
            Log.e(TAG, "saveUser: Write failed with error: ${mapped.message}")
            Result.failure(mapped)
        }
    }

    override suspend fun updateRole(uid: String, role: UserRole, primarySkill: String?): Result<Unit> = runCatching {
        val cleanUid = uid.trim()
        require(cleanUid.isNotBlank()) { "User UID cannot be empty when updating role" }
        Log.d(TAG, "updateRole: Setting role=${role.name} for uid=$cleanUid")

        val auth = FirebaseAuth.getInstance()
        val currentAuthUid = auth.currentUser?.uid
        if (currentAuthUid == null) {
            throw ProfileException.Unauthenticated()
        }
        if (currentAuthUid != cleanUid) {
            throw ProfileException.PermissionDenied()
        }

        val updateMap = mutableMapOf<String, Any>(
            "role" to role.name
        )
        if (!primarySkill.isNullOrBlank()) {
            updateMap["primarySkill"] = primarySkill.trim()
        }

        try {
            withTimeout(WRITE_TIMEOUT_MS) {
                usersCollection.document(cleanUid).update(updateMap as Map<String, Any>).awaitTask()
            }
            Log.i(TAG, "updateRole: Successfully updated role in Firestore for uid=$cleanUid")
        } catch (e: Exception) {
            Log.w(TAG, "updateRole: Failed to update role for uid=$cleanUid: ${e.message}")
            // If the document does not exist yet or update threw, fallback to merge
            if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                usersCollection.document(cleanUid).set(updateMap, SetOptions.merge()).awaitTask()
            } else {
                throw mapFirestoreException(e)
            }
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> = runCatching {
        val uid = user.uid.trim()
        require(uid.isNotBlank()) { "User UID cannot be empty when updating profile" }
        Log.d(TAG, "updateUser: Updating profile for uid=$uid, role=${user.role.name}")

        val data = userToMap(user)
        try {
            withTimeout(WRITE_TIMEOUT_MS) {
                usersCollection.document(uid).update(data).awaitTask()
            }
            Log.i(TAG, "updateUser: Succeeded for uid=$uid")
        } catch (e: Exception) {
            Log.w(TAG, "updateUser: Failed for uid=$uid: ${e.message}")
            throw mapFirestoreException(e)
        }
    }

    override fun observeUser(uid: String): Flow<User?> = callbackFlow {
        val cleanUid = uid.trim()
        if (cleanUid.isBlank()) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }
        val listenerRegistration = usersCollection.document(cleanUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "observeUser listener error for uid=$cleanUid: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshotToUser(snapshot.id, snapshot.data ?: emptyMap()))
                } else {
                    trySend(null)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    private suspend fun verifyDocumentExists(uid: String): Boolean {
        // Try reading from cache first (instant)
        try {
            val cacheSnapshot = runCatching {
                usersCollection.document(uid).get(Source.CACHE).awaitTask()
            }.getOrNull()

            if (cacheSnapshot != null && cacheSnapshot.exists()) {
                val cachedUser = snapshotToUser(uid, cacheSnapshot.data ?: emptyMap())
                if (cachedUser.isProfileComplete()) {
                    Log.d(TAG, "verifyDocumentExists: Document verified in local Firestore cache for uid=$uid")
                    return true
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "verifyDocumentExists: Cache check failed: ${e.message}")
        }

        // Try quick server check
        return try {
            val serverSnapshot = withTimeoutOrNull(VERIFY_TIMEOUT_MS) {
                usersCollection.document(uid).get().awaitTask()
            }
            if (serverSnapshot != null && serverSnapshot.exists()) {
                val serverUser = snapshotToUser(uid, serverSnapshot.data ?: emptyMap())
                serverUser.isProfileComplete()
            } else {
                false
            }
        } catch (e: Exception) {
            Log.w(TAG, "verifyDocumentExists: Server check failed: ${e.message}")
            false
        }
    }

    private fun mapFirestoreException(throwable: Throwable): ProfileException {
        return when {
            throwable is ProfileException -> throwable
            throwable is FirebaseFirestoreException -> {
                when (throwable.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                        ProfileException.PermissionDenied(throwable)
                    FirebaseFirestoreException.Code.UNAUTHENTICATED ->
                        ProfileException.Unauthenticated(throwable)
                    FirebaseFirestoreException.Code.UNAVAILABLE ->
                        ProfileException.NetworkUnavailable(throwable)
                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
                        ProfileException.Timeout(throwable)
                    FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED ->
                        ProfileException.QuotaExceeded(throwable)
                    FirebaseFirestoreException.Code.INVALID_ARGUMENT ->
                        ProfileException.InvalidData("अमान्य डेटा संरचना", throwable)
                    else ->
                        ProfileException.OperationFailed(throwable.localizedMessage ?: "Firestore error code ${throwable.code}", throwable)
                }
            }
            throwable is TimeoutCancellationException -> {
                ProfileException.Timeout(throwable)
            }
            throwable is com.google.firebase.FirebaseNetworkException ||
            throwable is java.net.UnknownHostException ||
            throwable is java.net.SocketTimeoutException ||
            throwable is java.io.IOException -> {
                ProfileException.NetworkUnavailable(throwable)
            }
            throwable is IllegalArgumentException || throwable is IllegalStateException -> {
                ProfileException.InvalidData(throwable.localizedMessage ?: "Validation failed", throwable)
            }
            else -> {
                ProfileException.OperationFailed(throwable.localizedMessage ?: "अज्ञात त्रुटि", throwable)
            }
        }
    }

    private fun userToMap(user: User): Map<String, Any?> = buildMap {
        put("uid", user.uid)
        put("fullName", user.fullName)
        put("mobileNumber", user.mobileNumber)
        put("location", user.location)
        put("role", user.role.name)
        put("primarySkill", user.primarySkill)
        put("createdAt", user.createdAt)
        put("termsAccepted", user.termsAccepted)
        if (user.termsAcceptedAt != null) {
            put("termsAcceptedAt", user.termsAcceptedAt)
        }
    }

    private fun snapshotToUser(id: String, map: Map<String, Any?>): User {
        val roleStr = map["role"] as? String ?: UserRole.WORKER.name
        val roleEnum = runCatching { UserRole.valueOf(roleStr) }.getOrDefault(UserRole.WORKER)
        val createdAt = when (val raw = map["createdAt"]) {
            is Number -> raw.toLong()
            is com.google.firebase.Timestamp -> raw.toDate().time
            else -> System.currentTimeMillis()
        }
        val termsAccepted = (map["termsAccepted"] as? Boolean) ?: false
        val termsAcceptedAt = (map["termsAcceptedAt"] as? Number)?.toLong()

        return User(
            uid = (map["uid"] as? String) ?: id,
            fullName = map["fullName"] as? String ?: "",
            mobileNumber = map["mobileNumber"] as? String ?: "",
            location = map["location"] as? String ?: "",
            role = roleEnum,
            primarySkill = map["primarySkill"] as? String ?: "",
            createdAt = createdAt,
            termsAccepted = termsAccepted,
            termsAcceptedAt = termsAcceptedAt
        )
    }
}
