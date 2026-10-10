package com.example.shramsarthi.data.knowledge

/**
 * Verified model representing official Labour Laws and Worker Rights.
 * Contains only verified government statutory regulations, portals, and helplines.
 */
data class LabourRightInfo(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val category: String, // WAGES, HOURS_OVERTIME, SAFETY, WELFARE, GRIEVANCE
    val keyPointsHindi: List<String>,
    val keyPointsEnglish: List<String>,
    val issuingAuthority: String,
    val officialLink: String,
    val officialHelpline: String = "",
    val applicableLaw: String,
    val lastVerifiedDate: String = "October 2026",
    val legalDisclaimer: String = "यह जानकारी सामान्य विधिक मार्गदर्शन के लिए है। किसी भी विवाद में विधिक सलाह या श्रम विभाग से संपर्क करें।"
)

object LabourRightsRepository {

    val verifiedRights: List<LabourRightInfo> = listOf(
        LabourRightInfo(
            id = "min_wage_and_payment",
            titleHindi = "न्यूनतम मजदूरी और समय पर भुगतान का अधिकार",
            titleEnglish = "Right to Minimum Wage & Timely Payment",
            category = "WAGES",
            keyPointsHindi = listOf(
                "कोई भी ठेकेदार या बिल्डर सरकार द्वारा तय न्यूनतम मजदूरी (Minimum Wage) से कम भुगतान नहीं कर सकता। कम पैसे देना कानूनन अपराध है।",
                "मजदूरी का भुगतान मासिक, पाक्षिक (15 दिन) या साप्ताहिक आधार पर तय समय (अगले माह की 7 से 10 तारीख तक) होना अनिवार्य है।",
                "मजदूरी बिना किसी अनुचित कटौती के सीधे कामगार के बैंक खाते में या नकद रसीद के साथ दी जानी चाहिए।"
            ),
            keyPointsEnglish = listOf(
                "No employer or contractor can pay below the notified government minimum wages. Underpayment is a cognizable legal offence.",
                "Wages must be paid by the 7th or 10th of the following wage period without illegal deductions.",
                "Workers are entitled to wage slips / register receipts stating full days worked and amounts paid."
            ),
            issuingAuthority = "Ministry of Labour & Employment (MoLE), Govt. of India",
            officialLink = "https://clc.gov.in/",
            officialHelpline = "1800-180-1111 (Shramik Helpline)",
            applicableLaw = "Code on Wages, 2019 & Minimum Wages Act, 1948"
        ),
        LabourRightInfo(
            id = "overtime_and_hours",
            titleHindi = "काम के घंटे, विश्राम और ओवरटाइम का दोहरा भुगतान",
            titleEnglish = "Working Hours, Rest Intervals & Double Overtime Pay",
            category = "HOURS_OVERTIME",
            keyPointsHindi = listOf(
                "सामान्य कार्य दिवस में अधिकतम 8 घंटे काम और 1 घंटे का विश्राम अंतराल (लंच/आराम) होता है। एक सप्ताह में 48 घंटे से अधिक सामान्य काम नहीं लिया जा सकता।",
                "8 घंटे से अधिक काम कराए जाने पर कामगार को उसकी सामान्य दैनिक दर का 2 गुना (Double Overtime) मिलना कानूनन अनिवार्य है।",
                "हर 6 दिन लगातार काम के बाद 1 दिन का सवैतनिक या अनिवार्य साप्ताहिक अवकाश (Weekly Rest) मिलना कामगार का अधिकार है।"
            ),
            keyPointsEnglish = listOf(
                "Standard working hours are capped at 8 hours per day and 48 hours per week with mandatory rest intervals.",
                "Any work beyond 8 hours per day must be compensated at twice the ordinary rate of wages (2x Overtime).",
                "A mandatory weekly day of rest is guaranteed after 6 consecutive working days."
            ),
            issuingAuthority = "Chief Labour Commissioner (Central) / State Labour Departments",
            officialLink = "https://labour.gov.in/",
            officialHelpline = "011-23710111",
            applicableLaw = "Building and Other Construction Workers (BOCW) Act, 1996 (Sec 28)"
        ),
        LabourRightInfo(
            id = "wage_theft_grievance",
            titleHindi = "मजदूरी न मिलने या रोके जाने पर शिकायत और समाधान",
            titleEnglish = "Grievance Procedure for Unpaid Wages (Wage Theft)",
            category = "GRIEVANCE",
            keyPointsHindi = listOf(
                "यदि ठेकेदार काम कराकर पैसे नहीं देता, तो श्रमिक समाधान (SAMADHAN) पोर्टल पर सीधे ऑनलाइन शिकायत दर्ज कर सकता है।",
                "श्रमिक अपने जिले के श्रम आयुक्त (Labour Commissioner) या उप श्रम आयुक्त (DLC) कार्यालय में फॉर्म 'VI' के तहत दावा याचिका दायर कर सकता है।",
                "दोषी नियोक्ता से बकाया राशि के साथ 10 गुना तक हर्जाना वसूलने का कानूनी प्रावधान है।"
            ),
            keyPointsEnglish = listOf(
                "Workers can file complaints directly on the Ministry's SAMADHAN portal or visit the district Labour Commissioner.",
                "Claims for unpaid wages can be submitted to the Authority under the Minimum Wages Act / Payment of Wages Act.",
                "Authorities can order compensation up to 10 times the unpaid wage amount against defaulting contractors."
            ),
            issuingAuthority = "Chief Labour Commissioner (CLC) & SAMADHAN Portal",
            officialLink = "https://samadhan.labour.gov.in/",
            officialHelpline = "1800-180-1111",
            applicableLaw = "Payment of Wages Act, 1936 & Code on Wages, 2019"
        ),
        LabourRightInfo(
            id = "site_safety_and_ppe",
            titleHindi = "कार्यस्थल पर सुरक्षा उपकरण (PPE) और दुर्घटना मुआवजा",
            titleEnglish = "Workplace Safety, Mandatory PPE & Accident Compensation",
            category = "SAFETY",
            keyPointsHindi = listOf(
                "बिल्डर या ठेकेदार को हर कामगार को मुफ्त सुरक्षा किट (हेलमेट, सेफ्टी बूट्स, ग्लव्स, ऊंचाई पर बेल्ट) देना अनिवार्य है। इसके पैसे कामगार से नहीं काटे जा सकते।",
                "साइट पर प्राथमिक चिकित्सा पेटी (First Aid), स्वच्छ पीने का पानी, शौचालय और छायादार विश्राम स्थल होना अनिवार्य है।",
                "कार्यस्थल पर दुर्घटना या चोट लगने पर इलाज का पूरा खर्च और कर्मचारी मुआवजा कानून के तहत तत्काल मुआवजा मिलना कामगार का कानूनी अधिकार है।"
            ),
            keyPointsEnglish = listOf(
                "Employers are legally obligated to provide free Personal Protective Equipment (helmets, harness, boots, gloves).",
                "First aid, potable drinking water, clean sanitary facilities, and shelter are mandatory on every active site.",
                "In case of on-site accident, emergency medical treatment and statutory compensation are guaranteed by law."
            ),
            issuingAuthority = "Directorate General of Factory Advice Service and Labour Institutes (DGFASLI)",
            officialLink = "https://dgfasli.gov.in/",
            officialHelpline = "112 (Emergency) / 1800-180-1111",
            applicableLaw = "BOCW Act, 1996 & Employees' Compensation Act, 1923"
        ),
        LabourRightInfo(
            id = "bocw_welfare_cess",
            titleHindi = "भवन निर्माण कर्मकार कल्याण बोर्ड (BOCW) के अधिकार व भत्ते",
            titleEnglish = "BOCW Welfare Board Registration & Entitlements",
            category = "WELFARE",
            keyPointsHindi = listOf(
                "जिस कामगार ने पिछले 12 महीनों में 90 दिन निर्माण कार्य किया है, वह राज्य BOCW बोर्ड में पंजीकृत होने का हकदार है।",
                "पंजीकृत कामगारों को औजार खरीदने की सहायता, पुत्री के विवाह हेतु अनुदान, मातृत्व लाभ और बच्चों की पढ़ाई हेतु छात्रवृत्ति मिलती है।",
                "कार्य के दौरान मृत्यु होने पर परिवार को ₹2 से ₹5 लाख तक की आर्थिक सहायता और स्थायी अपंगता पर सहायता दी जाती है।"
            ),
            keyPointsEnglish = listOf(
                "Construction workers who completed 90 days of work in the preceding 12 months are eligible for State BOCW registration.",
                "Entitlements include tool purchase grants, daughter's marriage assistance, maternity benefit, and education scholarships.",
                "Accidental death and permanent disability assistance ranging from ₹2 Lakh to ₹5 Lakh provided to registered workers."
            ),
            issuingAuthority = "State Building & Other Construction Workers Welfare Boards",
            officialLink = "https://labour.gov.in/bocw-portal",
            officialHelpline = "1800-180-1111",
            applicableLaw = "BOCW Welfare Cess Act, 1996 & State BOCW Rules"
        )
    )

    fun searchRights(query: String): List<LabourRightInfo> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return verifiedRights

        return verifiedRights.filter { info ->
            info.titleHindi.lowercase().contains(q) ||
            info.titleEnglish.lowercase().contains(q) ||
            info.category.lowercase().contains(q) ||
            info.applicableLaw.lowercase().contains(q) ||
            info.keyPointsHindi.any { it.lowercase().contains(q) } ||
            info.keyPointsEnglish.any { it.lowercase().contains(q) } ||
            (q.contains("wage") || q.contains("मजदूरी") || q.contains("वेतन")) && info.category == "WAGES" ||
            (q.contains("overtime") || q.contains("घंटे") || q.contains("समय")) && info.category == "HOURS_OVERTIME" ||
            (q.contains("शिकायत") || q.contains("पैसे नहीं") || q.contains("पैसा रोक") || q.contains("complaint")) && info.category == "GRIEVANCE" ||
            (q.contains("safety") || q.contains("सुरक्षा") || q.contains("चोट") || q.contains("दुर्घटना")) && info.category == "SAFETY" ||
            (q.contains("bocw") || q.contains("बोर्ड") || q.contains("कल्याण") || q.contains("योजना")) && info.category == "WELFARE"
        }
    }

    fun getRightById(id: String): LabourRightInfo? {
        return verifiedRights.find { it.id == id }
    }
}
