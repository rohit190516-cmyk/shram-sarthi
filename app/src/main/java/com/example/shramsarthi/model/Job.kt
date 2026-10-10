package com.example.shramsarthi.model

/**
 * Job Requirement entity posted by a Thekedar or Builder.
 */
data class Job(
    val jobId: String = "",
    val thekedarId: String = "",
    val title: String = "",
    val workerType: String = "",
    val description: String = "",
    val rate: Double = 0.0,
    val rateType: RateType = RateType.DAILY,
    val workersRequired: Int = 1,
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val status: JobStatus = JobStatus.OPEN,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Compensation structure for a Job requirement.
 */
enum class RateType(val label: String) {
    DAILY("Per Day / दिहाड़ी"),
    HOURLY("Per Hour / प्रति घंटा"),
    FIXED("Fixed Contract / मुश्त राशि");

    fun getLocalizedLabel(isHindi: Boolean): String = when (this) {
        DAILY -> if (isHindi) "प्रति दिन" else "Per Day"
        HOURLY -> if (isHindi) "प्रति घंटा" else "Per Hour"
        FIXED -> if (isHindi) "निश्चित अनुबंध" else "Fixed Contract"
    }

    fun getUnitLabel(isHindi: Boolean): String = when (this) {
        DAILY -> if (isHindi) "दिन" else "day"
        HOURLY -> if (isHindi) "घंटा" else "hour"
        FIXED -> if (isHindi) "काम" else "fixed"
    }
}

/**
 * Lifecycle status of a Job posting.
 */
enum class JobStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
