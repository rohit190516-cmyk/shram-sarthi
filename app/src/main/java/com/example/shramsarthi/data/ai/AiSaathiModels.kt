package com.example.shramsarthi.data.ai

import com.example.shramsarthi.data.knowledge.GovernmentSchemeInfo
import com.example.shramsarthi.data.knowledge.LabourRightInfo
import com.example.shramsarthi.model.Job

/**
 * Categorization of user requests processed by AI Saathi.
 */
enum class AiIntentType {
    FIND_WORK,
    GOVERNMENT_SCHEME,
    LABOUR_RIGHTS,
    CREATE_WORK_REQUEST,
    GENERAL_HELP
}

/**
 * Validated structured search parameters parsed from natural language.
 */
data class JobSearchFilter(
    val tradeOrSkill: String? = null,
    val location: String? = null,
    val maxRadiusKm: Double? = 25.0,
    val expectedWage: Double? = null,
    val preferredStartDate: String? = null
)

/**
 * Structured, professional work request draft generated from worker utterance.
 * Must be reviewed and confirmed by the worker before any sharing or submission.
 */
data class WorkRequestDraft(
    val workerName: String = "",
    val trade: String = "",
    val experienceYears: String = "अनुभवी / Experienced",
    val preferredLocation: String = "",
    val availableFrom: String = "तत्काल / Immediate",
    val expectedDailyWage: String = "",
    val contactPhone: String = "",
    val hindiText: String = "",
    val englishText: String = ""
)

/**
 * Unified response payload produced by AI Saathi.
 * Separates spoken audio output from structured verifiable cards.
 */
data class AiSaathiResponse(
    val spokenHindi: String,
    val displayTitleHindi: String,
    val displayTextHindi: String,
    val intentType: AiIntentType,
    val matchedJobs: List<Job> = emptyList(),
    val matchedSchemes: List<GovernmentSchemeInfo> = emptyList(),
    val matchedRights: List<LabourRightInfo> = emptyList(),
    val workRequestDraft: WorkRequestDraft? = null,
    val clarificationNeeded: String? = null,
    val lastVerifiedDate: String? = null,
    val officialSourceUrl: String? = null,
    val legalDisclaimer: String? = null
)

/**
 * Voice interaction UI state.
 */
sealed interface AiSaathiUiState {
    data object Idle : AiSaathiUiState
    data class Listening(val partialTranscript: String = "") : AiSaathiUiState
    data class Processing(val userQuery: String) : AiSaathiUiState
    data class Speaking(val response: AiSaathiResponse) : AiSaathiUiState
    data class Success(val response: AiSaathiResponse) : AiSaathiUiState
    data class Error(val errorMessage: String, val canRetry: Boolean = true) : AiSaathiUiState
}
