package com.example.shramsarthi.model

import com.example.shramsarthi.util.AppLanguage

/**
 * Supported categories for formal complaints and labour grievances.
 */
enum class ComplaintCategory(
    val hindiTitle: String,
    val englishTitle: String
) {
    UNPAID_WAGES("मजदूरी नहीं मिली", "Unpaid Wages"),
    UNFAIR_TREATMENT("अनुचित व्यवहार", "Unfair Treatment"),
    UNSAFE_WORKPLACE("असुरक्षित कार्यस्थल", "Unsafe Workplace"),
    WORK_DISPUTE("काम का विवाद", "Work Dispute"),
    DELAYED_PAYMENT("देरी से भुगतान", "Delayed Payment"),
    OTHER("अन्य समस्या", "Other Problem");

    fun getTitle(language: AppLanguage): String {
        return if (language == AppLanguage.HINDI) hindiTitle else englishTitle
    }
}

/**
 * Genuine lifecycle statuses for a complaint.
 * Drafts are NEVER labelled as submitted.
 */
enum class ComplaintStatus(
    val hindiLabel: String,
    val englishLabel: String
) {
    DRAFT("तैयार हुई - ड्राफ्ट तैयार है", "Draft Ready"),
    IN_PROGRESS("प्रगति में - जानकारी एकत्र की जा रही है", "In Progress"),
    SUBMITTED("जमा किया गया - श्रम विभाग में भेजा गया", "Submitted to Labour Dept");

    fun getLabel(language: AppLanguage): String {
        return if (language == AppLanguage.HINDI) hindiLabel else englishLabel
    }
}

/**
 * Domain entity representing a genuine worker grievance or complaint draft.
 */
data class Complaint(
    val complaintId: String = "",
    val ownerUid: String = "",
    val category: ComplaintCategory = ComplaintCategory.UNPAID_WAGES,
    val originalDescription: String = "",
    val language: String = "hi",
    val employerName: String = "",
    val workLocation: String = "",
    val workDates: String = "",
    val wageAgreed: String = "",
    val amountPaid: String = "",
    val amountClaimed: String = "",
    val requestedResolution: String = "",
    val draftContent: String = "",
    val evidenceReferences: List<String> = emptyList(),
    val applicableLegalAct: String = "Code on Wages, 2019 (मजदूरी संहिता, 2019)",
    val officialAuthority: String = "Office of District Labour Commissioner / श्रम विभाग",
    val officialAuthorityUrl: String = "https://samadhan.labour.gov.in",
    val status: ComplaintStatus = ComplaintStatus.DRAFT,
    val submissionDestination: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
