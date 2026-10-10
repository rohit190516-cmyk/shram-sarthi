package com.example.shramsarthi.data.firebase

import android.app.Activity
import com.example.shramsarthi.data.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    override fun getCurrentUserPhone(): String? = auth.currentUser?.phoneNumber

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun signOut(): Result<Unit> = runCatching {
        resendToken = null
        auth.signOut()
    }

    override fun observeAuthState(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun sendOtp(
        activity: Any,
        phoneNumber: String,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: (uid: String) -> Unit,
        onTimeout: ((verificationId: String) -> Unit)?,
        onError: (Exception) -> Unit
    ) {
        val act = activity as? Activity
        if (act == null) {
            onError(IllegalArgumentException("Activity parameter must be an android.app.Activity"))
            return
        }

        val cleanPhone = phoneNumber.trim().replace(" ", "").replace("-", "")
        val formattedNumber = if (cleanPhone.startsWith("+")) cleanPhone else "+91$cleanPhone"

        try {
            val builder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(formattedNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(act)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Instant verification (test numbers or Play Services SMS auto-retrieval)
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val uid = task.result?.user?.uid
                                    if (uid != null) {
                                        onAutoVerified(uid)
                                    } else {
                                        onError(IllegalStateException("Firebase User is null after auto-verification"))
                                    }
                                } else {
                                    onError(task.exception ?: Exception("Auto-verification sign-in failed"))
                                }
                            }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        onError(e)
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        resendToken = token
                        onCodeSent(verificationId)
                    }

                    override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                        onTimeout?.invoke(verificationId)
                    }
                })

            resendToken?.let { token ->
                builder.setForceResendingToken(token)
            }

            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        } catch (e: Exception) {
            onError(e)
        }
    }

    override suspend fun signInWithOtp(verificationId: String, otpCode: String): Result<String> = runCatching {
        withTimeout(15000L) {
            val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
            val authResult = auth.signInWithCredential(credential).awaitTask()
            authResult.user?.uid ?: throw IllegalStateException("Firebase User is null after OTP authentication")
        }
    }
}
