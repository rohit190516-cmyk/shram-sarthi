package com.example.shramsarthi.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Authentication states and user sessions.
 */
interface AuthRepository {
    fun getCurrentUserId(): String?
    fun getCurrentUserPhone(): String?
    fun isUserLoggedIn(): Boolean
    suspend fun signOut(): Result<Unit>
    fun observeAuthState(): Flow<String?>

    /**
     * Sends an SMS verification code to the specified phone number.
     * @param activity Android Activity required by Firebase PhoneAuth
     * @param phoneNumber International format phone number (or 10 digits)
     * @param onCodeSent Callback receiving the verificationId
     * @param onAutoVerified Callback receiving UID if phone is instantly verified (e.g. test phone numbers or instant verification)
     * @param onTimeout Callback receiving verificationId when auto-retrieval window expires
     * @param onError Callback receiving any failure exception
     */
    fun sendOtp(
        activity: Any,
        phoneNumber: String,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: (uid: String) -> Unit = {},
        onTimeout: ((verificationId: String) -> Unit)? = null,
        onError: (Exception) -> Unit
    )

    /**
     * Signs in using the verification ID and SMS OTP code.
     * @return Result containing the authenticated Firebase UID
     */
    suspend fun signInWithOtp(verificationId: String, otpCode: String): Result<String>
}
