package com.example.shramsarthi.model

/**
 * Core User entity representing any registered participant in Shram Sarthi.
 */
data class User(
    val uid: String = "",
    val fullName: String = "",
    val mobileNumber: String = "",
    val location: String = "",
    val role: UserRole = UserRole.WORKER,
    val primarySkill: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val termsAccepted: Boolean = false,
    val termsAcceptedAt: Long? = null
) {
    /**
     * Determines whether the user has completed their registration profile.
     * Requires valid UID, full name (>= 2 chars), location, accepted terms,
     * and a primary skill for Worker role.
     */
    fun isProfileComplete(): Boolean =
        uid.isNotBlank() &&
                fullName.trim().length >= 2 &&
                location.trim().isNotBlank() &&
                termsAccepted &&
                (role != UserRole.WORKER || primarySkill.trim().isNotBlank())
}
