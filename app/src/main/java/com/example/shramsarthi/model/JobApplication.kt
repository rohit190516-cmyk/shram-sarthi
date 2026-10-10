package com.example.shramsarthi.model

/**
 * Application record when a Worker applies to a Job.
 */
data class JobApplication(
    val applicationId: String = "",
    val jobId: String = "",
    val workerId: String = "",
    val thekedarId: String = "",
    val status: ApplicationStatus = ApplicationStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Status states for a JobApplication.
 */
enum class ApplicationStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    WITHDRAWN
}
