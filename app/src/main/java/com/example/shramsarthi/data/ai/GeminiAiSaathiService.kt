package com.example.shramsarthi.data.ai

import android.content.Context
import android.util.Log
import com.example.shramsarthi.data.knowledge.GovernmentSchemesRepository
import com.example.shramsarthi.data.knowledge.LabourRightsRepository
import com.example.shramsarthi.data.location.LocationService
import com.example.shramsarthi.data.repository.JobRepository
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.User
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.generationConfig
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

/**
 * Service managing Firebase AI Logic (Gemini 1.5 Flash) integration for Shram Sarthi AI Saathi.
 * Enforces verifiable factual separation, strict quota limits, input validation, and safe offline fallbacks.
 */
class GeminiAiSaathiService(
    private val context: Context,
    private val jobRepository: JobRepository,
    private val locationService: LocationService,
    private val defaultModelName: String = "gemini-1.5-flash"
) {

    companion object {
        private const val TAG = "GeminiAiSaathi"
        private const val MAX_INPUT_LENGTH = 500
        private const val REQUEST_TIMEOUT_MS = 12000L
        private const val MAX_DAILY_REQUESTS = 50
    }

    private val sharedPrefs = context.getSharedPreferences("ai_saathi_usage", Context.MODE_PRIVATE)

    /**
     * Lazy-initialized Firebase Vertex AI GenerativeModel.
     * Inherits Firebase App Check and securely authenticated backend without exposed raw API keys.
     */
    private val generativeModel by lazy {
        try {
            Firebase.vertexAI.generativeModel(
                modelName = defaultModelName,
                generationConfig = generationConfig {
                    temperature = 0.2f
                    topK = 32
                    topP = 0.85f
                    maxOutputTokens = 600
                }
            )
        } catch (e: Exception) {
            Log.w(TAG, "VertexAI initialization notice: ${e.message}")
            null
        }
    }

    /**
     * Processes a voice utterance or text query through AI Saathi.
     */
    suspend fun processQuery(
        userQuery: String,
        currentUser: User?
    ): AiSaathiResponse {
        val trimmedQuery = userQuery.trim().take(MAX_INPUT_LENGTH)
        if (trimmedQuery.isBlank()) {
            return AiSaathiResponse(
                spokenHindi = "कृपया बताइए, मैं आपकी क्या सहायता कर सकता हूँ?",
                displayTitleHindi = "सारथी सहायता",
                displayTextHindi = "कृपया बोलकर या लिखकर अपना प्रश्न पूछें। जैसे: 'मुझे राजमिस्त्री का काम चाहिए' या 'ई-श्रम कार्ड के फायदे'।",
                intentType = AiIntentType.GENERAL_HELP
            )
        }

        // Check local rate limits
        if (!checkAndIncrementUsageQuota()) {
            return AiSaathiResponse(
                spokenHindi = "आज की AI सेवा सीमा पूरी हो गई है। आप नीचे दिए गए सीधे विकल्पों से काम और योजनाएं देख सकते हैं।",
                displayTitleHindi = "दैनिक सीमा पूरी हुई / Usage Quota Reached",
                displayTextHindi = "आज के लिए आपकी अनुमत AI पूछताछ सीमा समाप्त हो गई है। सर्वर को सुरक्षित रखने के लिए यह सीमा लगाई गई है। आप सीधे डैशबोर्ड से काम देख सकते हैं।",
                intentType = AiIntentType.GENERAL_HELP
            )
        }

        // 1. First, attempt classification and structured extraction via Gemini (Firebase AI Logic)
        var parsedIntent: ParsedAiIntent? = null
        try {
            parsedIntent = withTimeout(REQUEST_TIMEOUT_MS) {
                queryGeminiForIntent(trimmedQuery, currentUser)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini call failed or timed out (${e.message}), utilizing local deterministic intent classifier.")
        }

        // 2. If Gemini is unavailable (offline/quota/timeout), use local verified parser
        val effectiveIntent = parsedIntent ?: fallbackLocalClassifier(trimmedQuery, currentUser)

        // 3. Dispatch to verified data services according to strict separation of concerns
        return when (effectiveIntent.intentType) {
            AiIntentType.FIND_WORK -> handleFindWorkIntent(effectiveIntent, trimmedQuery)
            AiIntentType.GOVERNMENT_SCHEME -> handleGovernmentSchemeIntent(effectiveIntent, trimmedQuery)
            AiIntentType.LABOUR_RIGHTS -> handleLabourRightsIntent(effectiveIntent, trimmedQuery)
            AiIntentType.CREATE_WORK_REQUEST -> handleCreateWorkRequestIntent(effectiveIntent, currentUser)
            AiIntentType.GENERAL_HELP -> handleGeneralHelpIntent(effectiveIntent)
        }
    }

    /**
     * Internal data carrier for structured intent representation.
     */
    private data class ParsedAiIntent(
        val intentType: AiIntentType,
        val trade: String? = null,
        val location: String? = null,
        val radiusKm: Double? = 25.0,
        val expectedWage: Double? = null,
        val schemeName: String? = null,
        val rightsTopic: String? = null,
        val spokenMessageHindi: String? = null,
        val displayMessageHindi: String? = null,
        val clarificationQuestion: String? = null
    )

    /**
     * Prompts Gemini to return a structured JSON classifying intent and extracting filters.
     */
    private suspend fun queryGeminiForIntent(query: String, currentUser: User?): ParsedAiIntent? {
        val model = generativeModel ?: return null

        val systemPrompt = """
            You are 'Shram Sarthi AI Saathi' (श्रम सारथी AI साथी), a trustworthy voice assistant for Indian construction workers.
            Analyze the user's Hindi or English request and output strictly valid JSON matching this schema:
            {
              "intent": "FIND_WORK" | "GOVERNMENT_SCHEME" | "LABOUR_RIGHTS" | "CREATE_WORK_REQUEST" | "GENERAL_HELP",
              "trade": string or null (e.g. "राजमिस्त्री", "प्लम्बर", "बढ़ई", "हेल्पर"),
              "location": string or null (e.g. "Greater Noida", "Jaipur", "Noida"),
              "expectedWage": number or null,
              "schemeName": string or null (e.g. "eshram", "pm_sym", "pm_vishwakarma", "ayushman_bharat"),
              "rightsTopic": string or null (e.g. "wages", "overtime", "unpaid", "safety", "bocw"),
              "spokenHindi": concise 1-sentence Hindi response for Text-To-Speech (max 20 words),
              "clarification": string or null if vital info is missing
            }
            CRITICAL RULES:
            - Never invent jobs, employers, laws, or benefits.
            - Spoken Hindi must be very polite, respectful, and simple (like addressing a brother/shramik).
            User Context:
            - Worker Default Skill: ${currentUser?.primarySkill?.takeIf { it.isNotBlank() } ?: "General Construction"}
            - Worker City: ${currentUser?.location ?: ""}
            User Utterance: "$query"
        """.trimIndent()

        val response = model.generateContent(systemPrompt)
        val text = response.text?.trim() ?: return null

        // Extract JSON block
        val jsonStart = text.indexOf('{')
        val jsonEnd = text.lastIndexOf('}')
        if (jsonStart == -1 || jsonEnd == -1 || jsonEnd <= jsonStart) return null

        val jsonStr = text.substring(jsonStart, jsonEnd + 1)
        val json = JSONObject(jsonStr)

        val intentStr = json.optString("intent", "GENERAL_HELP")
        val intentType = try {
            AiIntentType.valueOf(intentStr)
        } catch (_: Exception) {
            AiIntentType.GENERAL_HELP
        }

        return ParsedAiIntent(
            intentType = intentType,
            trade = json.optString("trade").takeIf { it.isNotBlank() && it != "null" },
            location = json.optString("location").takeIf { it.isNotBlank() && it != "null" },
            expectedWage = if (json.has("expectedWage")) json.optDouble("expectedWage").takeIf { !it.isNaN() } else null,
            schemeName = json.optString("schemeName").takeIf { it.isNotBlank() && it != "null" },
            rightsTopic = json.optString("rightsTopic").takeIf { it.isNotBlank() && it != "null" },
            spokenMessageHindi = json.optString("spokenHindi").takeIf { it.isNotBlank() && it != "null" },
            clarificationQuestion = json.optString("clarification").takeIf { it.isNotBlank() && it != "null" }
        )
    }

    /**
     * Fallback local deterministic classifier that operates offline without any network or Gemini dependency.
     */
    private fun fallbackLocalClassifier(query: String, currentUser: User?): ParsedAiIntent {
        val q = query.lowercase().trim()

        // 1. Check Work Request Creation
        if (q.contains("बायोडाटा") || q.contains("फॉर्म") || q.contains("कार्ड बना") || q.contains("निवेदन") ||
            (q.contains("चाहिए") && (q.contains("कल से") || q.contains("सोमवार") || q.contains("तुरंत")))
        ) {
            return ParsedAiIntent(
                intentType = AiIntentType.CREATE_WORK_REQUEST,
                trade = extractTradeFromText(q) ?: currentUser?.primarySkill?.takeIf { it.isNotBlank() },
                location = extractLocationFromText(q) ?: currentUser?.location?.takeIf { it.isNotBlank() }
            )
        }

        // 2. Check Labour Rights & Laws
        if (q.contains("कानून") || q.contains("मजदूरी") || q.contains("न्यूनतम") || q.contains("पैसे नहीं") ||
            q.contains("पैसा रोक") || q.contains("घंटे") || q.contains("overtime") || q.contains("ओवरटाइम") ||
            q.contains("सुरक्षा") || q.contains("चोट") || q.contains("शिकायत") || q.contains("हेल्पलाइन")
        ) {
            return ParsedAiIntent(
                intentType = AiIntentType.LABOUR_RIGHTS,
                rightsTopic = q
            )
        }

        // 3. Check Government Schemes
        if (q.contains("योजना") || q.contains("ई-श्रम") || q.contains("eshram") || q.contains("पेंशन") ||
            q.contains("मानधन") || q.contains("विश्वकर्मा") || q.contains("आयुष्मान") || q.contains("कार्ड") ||
            q.contains("सरकारी") || q.contains("बीमा")
        ) {
            return ParsedAiIntent(
                intentType = AiIntentType.GOVERNMENT_SCHEME,
                schemeName = q
            )
        }

        // 4. Check Job Search
        if (q.contains("काम") || q.contains("नौकरी") || q.contains("job") || q.contains("work") ||
            q.contains("मिस्त्री") || q.contains("प्लम्बर") || q.contains("बढ़ई") || q.contains("लेबर")
        ) {
            return ParsedAiIntent(
                intentType = AiIntentType.FIND_WORK,
                trade = extractTradeFromText(q) ?: currentUser?.primarySkill?.takeIf { it.isNotBlank() },
                location = extractLocationFromText(q) ?: currentUser?.location?.takeIf { it.isNotBlank() }
            )
        }

        return ParsedAiIntent(
            intentType = AiIntentType.GENERAL_HELP,
            spokenMessageHindi = "सारथी आपके साथ है। आप काम खोजने, सरकारी योजनाओं या मजदूरी अधिकारों के बारे में पूछ सकते हैं।"
        )
    }

    private fun extractTradeFromText(q: String): String? {
        val trades = mapOf(
            "राजमिस्त्री" to "राजमिस्त्री / Mason",
            "mistri" to "राजमिस्त्री / Mason",
            "mason" to "राजमिस्त्री / Mason",
            "प्लम्बर" to "प्लम्बर / Plumber",
            "plumber" to "प्लम्बर / Plumber",
            "बढ़ई" to "बढ़ई / Carpenter",
            "carpenter" to "बढ़ई / Carpenter",
            "पेंटर" to "पेंटर / Painter",
            "painter" to "पेंटर / Painter",
            "इलेक्ट्रीशियन" to "इलेक्ट्रीशियन / Electrician",
            "electrician" to "इलेक्ट्रीशियन / Electrician",
            "वेल्डर" to "वेल्डर / Welder",
            "welder" to "वेल्डर / Welder",
            "मजदूर" to "मजदूर / Helper",
            "helper" to "मजदूर / Helper",
            "बेलदार" to "मजदूर / Helper",
            "टाइल" to "टाइल मिस्त्री / Tile Mason"
        )
        for ((key, value) in trades) {
            if (q.contains(key)) return value
        }
        return null
    }

    private fun extractLocationFromText(q: String): String? {
        for ((city, _) in locationService.knownCityCenters) {
            if (q.contains(city)) return city.replaceFirstChar { it.uppercase() }
        }
        return null
    }

    /**
     * Executes authentic Firestore Job search according to parsed trade and location.
     */
    private suspend fun handleFindWorkIntent(
        intent: ParsedAiIntent,
        rawQuery: String
    ): AiSaathiResponse {
        val allOpenJobsResult = jobRepository.getOpenJobs()
        val allJobs = allOpenJobsResult.getOrDefault(emptyList())

        val tradeFilter = intent.trade
        val locationFilter = intent.location

        // Resolve reference location point for actual distance filtering
        val originPoint = if (!locationFilter.isNullOrBlank()) {
            locationService.resolveCoordinates(locationFilter)
        } else {
            locationService.getDeviceLastKnownLocation()
        }

        val filteredJobs = allJobs.filter { job ->
            var matchesTrade = true
            if (!tradeFilter.isNullOrBlank()) {
                val t = tradeFilter.lowercase()
                matchesTrade = job.workerType.lowercase().contains(t) ||
                        job.title.lowercase().contains(t) ||
                        t.contains(job.workerType.lowercase())
            }

            var matchesLocation = true
            if (!locationFilter.isNullOrBlank()) {
                if (originPoint != null) {
                    matchesLocation = locationService.isWithinRadius(originPoint, job.location, intent.radiusKm ?: 30.0)
                } else {
                    matchesLocation = job.location.lowercase().contains(locationFilter.lowercase())
                }
            }

            matchesTrade && matchesLocation
        }

        val spokenMsg = if (filteredJobs.isNotEmpty()) {
            "मुझे आपके लिए ${filteredJobs.size} सत्यापित काम मिले हैं। विवरण नीचे कार्ड पर देखें।"
        } else {
            val locLabel = locationFilter ?: "आपके क्षेत्र"
            "वर्तमान में $locLabel में कोई खाली काम उपलब्ध नहीं है। आप खोज क्षेत्र बढ़ा सकते हैं।"
        }

        val title = if (tradeFilter != null) "सत्यापित काम: $tradeFilter" else "सत्यापित उपलब्ध काम"
        val subtitle = if (filteredJobs.isNotEmpty()) {
            "फायरस्टोर डेटाबेस से मिले वास्तविक काम (${filteredJobs.size} उपलब्ध)"
        } else {
            "शर्तों से मेल खाता कोई काम नहीं मिला। कृपया दायरा बढ़ाएं या अन्य ट्रेड चुनें।"
        }

        return AiSaathiResponse(
            spokenHindi = spokenMsg,
            displayTitleHindi = title,
            displayTextHindi = subtitle,
            intentType = AiIntentType.FIND_WORK,
            matchedJobs = filteredJobs,
            lastVerifiedDate = "Real-time Cloud Firestore",
            legalDisclaimer = "सभी नौकरियां ठेकेदारों द्वारा सीधे पोस्ट की गई हैं। आवेदन करने से पहले काम के नियम और साइट अवश्य जांचें।"
        )
    }

    /**
     * Retrieves verified Government Schemes based on query relevance.
     */
    private fun handleGovernmentSchemeIntent(
        intent: ParsedAiIntent,
        rawQuery: String
    ): AiSaathiResponse {
        val q = intent.schemeName ?: rawQuery
        val matched = GovernmentSchemesRepository.searchSchemes(q)

        val first = matched.firstOrNull() ?: GovernmentSchemesRepository.verifiedSchemes.first()
        val spokenMsg = "मैंने ${first.nameHindi} की सत्यापित सरकारी जानकारी ढूंढ ली है।"

        return AiSaathiResponse(
            spokenHindi = spokenMsg,
            displayTitleHindi = first.nameHindi,
            displayTextHindi = first.purposeHindi,
            intentType = AiIntentType.GOVERNMENT_SCHEME,
            matchedSchemes = matched,
            lastVerifiedDate = first.lastVerifiedDate,
            officialSourceUrl = first.officialApplicationUrl,
            legalDisclaimer = first.eligibilityDisclaimer
        )
    }

    /**
     * Retrieves verified Labour Rights and Statutory regulations based on user inquiry.
     */
    private fun handleLabourRightsIntent(
        intent: ParsedAiIntent,
        rawQuery: String
    ): AiSaathiResponse {
        val q = intent.rightsTopic ?: rawQuery
        val matched = LabourRightsRepository.searchRights(q)

        val first = matched.firstOrNull() ?: LabourRightsRepository.verifiedRights.first()
        val spokenMsg = "आपके अधिकारों और सरकारी नियमों की सत्यापित जानकारी नीचे प्रदर्शित है।"

        return AiSaathiResponse(
            spokenHindi = spokenMsg,
            displayTitleHindi = first.titleHindi,
            displayTextHindi = first.keyPointsHindi.joinToString("\n\n• ", prefix = "• "),
            intentType = AiIntentType.LABOUR_RIGHTS,
            matchedRights = matched,
            lastVerifiedDate = first.lastVerifiedDate,
            officialSourceUrl = first.officialLink,
            legalDisclaimer = first.legalDisclaimer
        )
    }

    /**
     * Generates a formal, respectful Work Request draft for the worker.
     */
    private fun handleCreateWorkRequestIntent(
        intent: ParsedAiIntent,
        currentUser: User?
    ): AiSaathiResponse {
        val name = currentUser?.fullName?.takeIf { it.isNotBlank() } ?: "कारीगर साथी"
        val trade = intent.trade ?: currentUser?.primarySkill?.takeIf { it.isNotBlank() } ?: "कुशल निर्माण कामगार"
        val location = intent.location ?: currentUser?.location?.takeIf { it.isNotBlank() } ?: "एनसीआर / निकटवर्ती क्षेत्र"
        val wageStr = intent.expectedWage?.let { "₹${it.toInt()} प्रति दिन" } ?: "परस्पर सहमति / सरकार द्वारा तय न्यूनतम दर"
        val phone = currentUser?.mobileNumber ?: ""

        val hindiText = """
            【 औपचारिक कार्य उपलब्धता आवेदन / Work Availability Request 】
            
            मान्यवर ठेकेदार / बिल्डर,
            मेरा नाम $name है। मैं $trade के पद पर कार्य हेतु उपलब्ध हूँ।
            
            • ट्रेड / कार्य: $trade
            • अनुभव / कौशल: सत्यापित व अनुभवी
            • पसंदीदा स्थान: $location
            • कार्य प्रारंभ: तत्काल / आगामी कार्य दिवस
            • अपेक्षित दिहाड़ी: $wageStr
            • संपर्क सूत्र: $phone
            
            यदि आपकी निर्माण साइट पर आवश्यकता हो, तो कृपया संपर्क करें।
        """.trimIndent()

        val englishText = """
            [ Formal Work Availability Notice ]
            Name: $name
            Trade: $trade
            Preferred Location: $location
            Availability: Immediate / Upcoming schedule
            Expected Rate: $wageStr
            Contact: $phone
        """.trimIndent()

        val draft = WorkRequestDraft(
            workerName = name,
            trade = trade,
            experienceYears = "अनुभवी कारीगर",
            preferredLocation = location,
            availableFrom = "तत्काल / Immediate",
            expectedDailyWage = wageStr,
            contactPhone = phone,
            hindiText = hindiText,
            englishText = englishText
        )

        return AiSaathiResponse(
            spokenHindi = "मैंने आपका कार्य उपलब्धता आवेदन तैयार कर दिया है। आप नीचे देखकर इसे शेयर कर सकते हैं।",
            displayTitleHindi = "औपचारिक कार्य उपलब्धता आवेदन",
            displayTextHindi = "आपका कार्य विवरण तैयार है। इसे ठेकेदारों या व्हाट्सएप्प पर साझा करने से पहले जांच लें।",
            intentType = AiIntentType.CREATE_WORK_REQUEST,
            workRequestDraft = draft,
            legalDisclaimer = "यह केवल आपके द्वारा सत्यापित प्रारूप है। आपकी पुष्टि के बिना कोई संदेश स्वतः नहीं भेजा जाता।"
        )
    }

    private fun handleGeneralHelpIntent(intent: ParsedAiIntent): AiSaathiResponse {
        val spoken = intent.spokenMessageHindi ?: "नमस्कार! मैं श्रम सारथी AI साथी हूँ। बोलिए, मैं क्या मदद करूँ?"
        return AiSaathiResponse(
            spokenHindi = spoken,
            displayTitleHindi = "श्रम सारथी AI साथी से पूछें",
            displayTextHindi = "आप सीधे बोलकर या लिखकर मदद ले सकते हैं:\n• 'ग्रेटर नोएडा में राजमिस्त्री का काम खोजें'\n• 'ई-श्रम कार्ड बनवाने के क्या फायदे हैं?'\n• 'अगर ठेकेदार पैसे न दे तो क्या करें?'\n• 'मेरे लिए काम का आवेदन बनाओ'",
            intentType = AiIntentType.GENERAL_HELP,
            legalDisclaimer = "AI साथी केवल सत्यापित सरकारी स्रोतों और वास्तविक जॉब्स पर आधारित जानकारी प्रदान करता है।"
        )
    }

    private fun checkAndIncrementUsageQuota(): Boolean {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
        val lastDate = sharedPrefs.getString("usage_date", "")
        var count = sharedPrefs.getInt("usage_count", 0)

        if (lastDate != today) {
            count = 0
            sharedPrefs.edit()
                .putString("usage_date", today)
                .putInt("usage_count", 1)
                .apply()
            return true
        }

        if (count >= MAX_DAILY_REQUESTS) {
            return false
        }

        sharedPrefs.edit().putInt("usage_count", count + 1).apply()
        return true
    }
}
