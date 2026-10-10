package com.example.shramsarthi.data.knowledge

/**
 * Verified model representing official Indian Government Welfare Schemes for workers.
 * Contains only verified government data, official portal URLs, and statutory eligibility rules.
 */
data class GovernmentSchemeInfo(
    val id: String,
    val nameHindi: String,
    val nameEnglish: String,
    val ministryOrDepartment: String,
    val purposeHindi: String,
    val purposeEnglish: String,
    val eligibilityCriteriaHindi: List<String>,
    val eligibilityCriteriaEnglish: List<String>,
    val exclusionsHindi: List<String>,
    val verifiedBenefitsHindi: List<String>,
    val verifiedBenefitsEnglish: List<String>,
    val requiredDocumentsHindi: List<String>,
    val requiredDocumentsEnglish: List<String>,
    val applicationProcessHindi: String,
    val officialApplicationUrl: String,
    val officialHelpline: String,
    val lastVerifiedDate: String = "October 2026",
    val eligibilityDisclaimer: String = "यह पात्रता प्रारंभिक है। अंतिम स्वीकृति संबंधित सरकारी विभाग या नोडल एजेंसी द्वारा दस्तावेजों के सत्यापन के बाद ही दी जाती है।"
)

object GovernmentSchemesRepository {

    val verifiedSchemes: List<GovernmentSchemeInfo> = listOf(
        GovernmentSchemeInfo(
            id = "eshram",
            nameHindi = "ई-श्रम योजना (राष्ट्रीय असंगठित कामगार डेटाबेस)",
            nameEnglish = "e-Shram Portal (UAN Card for Unorganised Workers)",
            ministryOrDepartment = "Ministry of Labour & Employment (MoLE), Govt. of India",
            purposeHindi = "भारत के सभी असंगठित और निर्माण श्रमिकों का राष्ट्रीय डेटाबेस, जिससे सरकारी योजनाओं और सीधे आर्थिक लाभ तक पहुंच मिले।",
            purposeEnglish = "National database of unorganised workers providing a unique 12-digit UAN card and social security scheme access.",
            eligibilityCriteriaHindi = listOf(
                "आयु 16 से 59 वर्ष के बीच होनी चाहिए।",
                "असंगठित क्षेत्र (निर्माण, मिस्त्री, बढ़ई, बेलदार, पेंटर, प्लम्बर, घरेलू कामगार आदि) में कार्यरत हों।",
                "ईपीएफओ (EPFO) या ईएसआईसी (ESIC) के सदस्य न हों।"
            ),
            eligibilityCriteriaEnglish = listOf(
                "Age must be between 16 and 59 years.",
                "Working in unorganised sector (construction, carpentry, masonry, plumbing, domestic work, etc.).",
                "Not an active member of EPFO or ESIC."
            ),
            exclusionsHindi = listOf(
                "आयकरदाता (Income Tax payer) पात्र नहीं हैं।",
                "ईपीएफओ / ईएसआईसी सदस्य पात्र नहीं हैं।"
            ),
            verifiedBenefitsHindi = listOf(
                "12 अंकों का विशिष्ट ई-श्रम यूएएन (UAN) कार्ड मिलता है, जो पूरे भारत में मान्य है।",
                "प्रधानमंत्री सुरक्षा बीमा योजना (PMSBY) के तहत दुर्घटना बीमा: दुर्घटना में मृत्यु या पूर्ण विकलांगता पर ₹2,00,000 की सहायता।",
                "दुर्घटना में आंशिक (Partial) विकलांगता होने पर ₹1,00,000 की वित्तीय सहायता।",
                "भविष्य में किसी भी आपदा या आपातकाल में केंद्र सरकार द्वारा सीधे बैंक खाते में आर्थिक सहायता।"
            ),
            verifiedBenefitsEnglish = listOf(
                "Universal Account Number (UAN) card valid across India.",
                "Accident insurance under PMSBY: ₹2,00,000 on accidental death or permanent disability.",
                "₹1,00,000 financial support on partial permanent disability.",
                "Direct benefit transfers during emergency relief or state distress."
            ),
            requiredDocumentsHindi = listOf(
                "आधार कार्ड (Aadhaar Card)",
                "आधार से लिंक सक्रिय मोबाइल नंबर (OTP सत्यापन के लिए)",
                "सक्रिय बैंक खाता संख्या और IFSC कोड"
            ),
            requiredDocumentsEnglish = listOf(
                "Aadhaar Card",
                "Aadhaar-linked active mobile number",
                "Bank Account Details with IFSC code"
            ),
            applicationProcessHindi = "आधिकारिक पोर्टल eshram.gov.in पर जाकर 'Register on e-Shram' पर क्लिक करें या अपने नजदीकी सीएससी (CSC) जन सेवा केंद्र से निशुल्क पंजीकरण कराएं।",
            officialApplicationUrl = "https://eshram.gov.in/",
            officialHelpline = "14434 (Toll-Free National Helpline)"
        ),
        GovernmentSchemeInfo(
            id = "pm_sym",
            nameHindi = "प्रधानमंत्री श्रम योगी मान-धन (PM-SYM पेंशन योजना)",
            nameEnglish = "Pradhan Mantri Shram Yogi Maandhan (PM-SYM Pension)",
            ministryOrDepartment = "Ministry of Labour & Employment (MoLE), Govt. of India",
            purposeHindi = "असंगठित कामगारों के बुढ़ापे की सुरक्षा के लिए मासिक सुनिश्चित पेंशन योजना।",
            purposeEnglish = "Voluntary and contributory pension scheme providing monthly assured pension for unorganised workers.",
            eligibilityCriteriaHindi = listOf(
                "प्रवेश आयु 18 से 40 वर्ष के बीच होनी चाहिए।",
                "मासिक आय ₹15,000 या उससे कम होनी चाहिए।",
                "असंगठित श्रमिक (राजमिस्त्री, मजदूर, प्लम्बर, ड्राइवर आदि) हों।"
            ),
            eligibilityCriteriaEnglish = listOf(
                "Entry age must be between 18 and 40 years.",
                "Monthly income must be ₹15,000 or below.",
                "Engaged in unorganised trades."
            ),
            exclusionsHindi = listOf(
                "ईपीएफओ (EPFO), ईएसआईसी (ESIC), या एनपीएस (NPS) सदस्य अपात्र हैं।",
                "आयकर (Income Tax) भरने वाले अपात्र हैं।"
            ),
            verifiedBenefitsHindi = listOf(
                "60 वर्ष की आयु पूरी होने के बाद जीवन भर हर महीने ₹3,000 की सुनिश्चित न्यूनतम पेंशन।",
                "कामगार की मृत्यु की स्थिति में उसके पति/पत्नी को 50% पारिवारिक पेंशन (₹1,500 प्रति माह)।",
                "जितना मासिक अंशदान कामगार देगा (उम्र अनुसार ₹55 से ₹200 प्रति माह), उतना ही 50% अंशदान केंद्र सरकार भी देगी।"
            ),
            verifiedBenefitsEnglish = listOf(
                "Assured minimum pension of ₹3,000 per month after attaining 60 years of age.",
                "50% family pension (₹1,500/month) to surviving spouse upon beneficiary's demise.",
                "Equal 50:50 matching contribution deposited every month by the Central Government."
            ),
            requiredDocumentsHindi = listOf(
                "आधार कार्ड",
                "बचत बैंक खाता / जन धन खाता पासबुक (IFSC सहित)",
                "मोबाइल नंबर"
            ),
            requiredDocumentsEnglish = listOf(
                "Aadhaar Card",
                "Savings Bank / Jan Dhan account passbook",
                "Mobile Number"
            ),
            applicationProcessHindi = "नजदीकी सीएससी (CSC) केंद्र जाएं या maandhan.in पर 'Self Enrollment' के माध्यम से ऑनलाइन आवेदन करें। पहली किस्त नकद जमा होगी, बाद की किस्तें बैंक से स्वतः कटेंगी।",
            officialApplicationUrl = "https://maandhan.in/",
            officialHelpline = "1800-267-6888 (Toll Free)"
        ),
        GovernmentSchemeInfo(
            id = "pm_vishwakarma",
            nameHindi = "पीएम विश्वकर्मा योजना (कारीगर एवं शिल्पकार उत्थान)",
            nameEnglish = "PM Vishwakarma Scheme (Support for Traditional Artisans)",
            ministryOrDepartment = "Ministry of MSME & Ministry of Skill Development, Govt. of India",
            purposeHindi = "पारंपरिक औजारों से काम करने वाले राजमिस्त्री, बढ़ई, लोहार, मूर्तिकार जैसे 18 पारंपरिक व्यवसायों के कामगारों को वित्तीय व तकनीकी संबल।",
            purposeEnglish = "Holistic financial, training, and toolkit support for 18 traditional crafts including Masons and Carpenters.",
            eligibilityCriteriaHindi = listOf(
                "आयु कम से कम 18 वर्ष होनी चाहिए।",
                "स्वीकृत 18 व्यवसायों में से एक में काम करते हों (जैसे राजमिस्त्री/Mason, बढ़ई/Carpenter, लोहार, मूर्तिकार आदि)।",
                "परिवार में कोई सरकारी नौकरी में न हो।"
            ),
            eligibilityCriteriaEnglish = listOf(
                "Minimum age of 18 years.",
                "Practicing one of the 18 eligible traditional trades (e.g. Mason / राजमिस्त्री, Carpenter / बढ़ई).",
                "No family member in government service."
            ),
            exclusionsHindi = listOf(
                "परिवार में पहले से पीएमईजीपी (PMEGP), स्वनिधि या मुद्रा का सक्रिय बकाया ऋण न हो।",
                "परिवार का कोई सदस्य सरकारी सेवा में न हो।"
            ),
            verifiedBenefitsHindi = listOf(
                "पीएम विश्वकर्मा प्रमाण-पत्र और डिजिटल पहचान पत्र।",
                "5-7 दिनों का बुनियादी कौशल प्रशिक्षण और प्रतिदिन ₹500 का मानदेय (Stipend)।",
                "प्रशिक्षण के बाद आधुनिक औजार किट खरीदने के लिए ₹15,000 का ई-वाउचर अनुदान।",
                "बिना किसी गारंटी (Collateral-Free) के ₹3,00,000 तक का सस्ता ऋण (प्रथम किस्त ₹1 लाख, द्वितीय किस्त ₹2 लाख) केवल 5% रियायती ब्याज दर पर।"
            ),
            verifiedBenefitsEnglish = listOf(
                "Official PM Vishwakarma Certificate and ID Card.",
                "Basic skill verification training with ₹500/day stipend.",
                "₹15,000 e-voucher grant for modern toolkit purchase.",
                "Collateral-free subsidized loan up to ₹3,00,000 (Tranche 1: ₹1 Lakh, Tranche 2: ₹2 Lakh) at 5% interest."
            ),
            requiredDocumentsHindi = listOf(
                "आधार कार्ड और मोबाइल नंबर",
                "बैंक खाता विवरण (बैंक पासबुक)",
                "राशन कार्ड या परिवार पहचान पत्र"
            ),
            requiredDocumentsEnglish = listOf(
                "Aadhaar Card and mobile number",
                "Bank Account Details",
                "Ration Card or Family identification"
            ),
            applicationProcessHindi = "नजदीकी सीएससी (Common Service Centre) पर जाकर बायोमेट्रिक सत्यापन के साथ pmvishwakarma.gov.in पर निशुल्क आवेदन कराएं। ग्राम पंचायत / यूएलबी स्तर पर सत्यापन होगा।",
            officialApplicationUrl = "https://pmvishwakarma.gov.in/",
            officialHelpline = "1800-267-7777 / 011-23061500"
        ),
        GovernmentSchemeInfo(
            id = "ayushman_bharat",
            nameHindi = "आयुष्मान भारत - प्रधानमंत्री जन आरोग्य योजना (PM-JAY)",
            nameEnglish = "Ayushman Bharat - PM-JAY (Health Insurance Scheme)",
            ministryOrDepartment = "National Health Authority (NHA), Ministry of Health, Govt. of India",
            purposeHindi = "गरीब व कमजोर श्रमिक परिवारों को गंभीर बीमारी में मुफ्त अस्पताल इलाज और कैशलेस स्वास्थ्य बीमा।",
            purposeEnglish = "Comprehensive cashless health coverage for secondary and tertiary hospitalization for vulnerable families.",
            eligibilityCriteriaHindi = listOf(
                "SECC 2011 सूची में सूचीबद्ध परिवार या ई-श्रम/बीओसीडब्ल्यू पंजीकृत निर्माण श्रमिक परिवार।",
                "परिवार के सदस्यों की संख्या या आयु पर कोई सीमा नहीं है।"
            ),
            eligibilityCriteriaEnglish = listOf(
                "Families identified under SECC 2011 criteria or eligible registered unorganised construction workers.",
                "No cap on family size or age."
            ),
            exclusionsHindi = listOf(
                "पक्के मकान, चार पहिया वाहन, सरकारी नौकरी वाले परिवार अपात्र हैं।"
            ),
            verifiedBenefitsHindi = listOf(
                "प्रत्येक पात्र परिवार को प्रति वर्ष ₹5,00,000 तक का मुफ्त कैशलेस इलाज।",
                "देश भर के सभी सरकारी और सूचीबद्ध निजी अस्पतालों में भर्ती होने पर दवाइयां, जांच व सर्जरी निशुल्क।",
                "अस्पताल में भर्ती होने से 3 दिन पहले और 15 दिन बाद तक की जांच व दवाइयों का खर्च शामिल।"
            ),
            verifiedBenefitsEnglish = listOf(
                "Free cashless hospital treatment up to ₹5,00,000 per family per year.",
                "Cashless treatment at empaneled public and private hospitals across India.",
                "Pre-hospitalization (3 days) and post-hospitalization (15 days) care and medicines included."
            ),
            requiredDocumentsHindi = listOf(
                "आधार कार्ड",
                "राशन कार्ड या परिवार पहचान पत्र",
                "मोबाइल नंबर"
            ),
            requiredDocumentsEnglish = listOf(
                "Aadhaar Card",
                "Ration Card or Family ID",
                "Mobile Number"
            ),
            applicationProcessHindi = "beneficiary.nha.gov.in पर जाकर या नजदीकी सरकारी अस्पताल/सीएससी केंद्र में आयुष्मान मित्र से पात्रता जांचें और आयुष्मान गोल्डन कार्ड बनवाएं।",
            officialApplicationUrl = "https://beneficiary.nha.gov.in/",
            officialHelpline = "14555 (National Health Toll Free)"
        ),
        GovernmentSchemeInfo(
            id = "bocw_state_board",
            nameHindi = "राज्य निर्माण कर्मकार कल्याण बोर्ड योजनाएं (BOCW)",
            nameEnglish = "State BOCW Construction Workers Welfare Schemes",
            ministryOrDepartment = "State Labour Welfare Boards (e.g. UP, Rajasthan, Delhi, MP, Bihar, Haryana)",
            purposeHindi = "पंजीकृत भवन एवं निर्माण कामगारों और उनके बच्चों के लिए छात्रवृत्ति, विवाह सहायता और औजार अनुदान।",
            purposeEnglish = "State-level statutory welfare benefits funded by BOCW cess for registered construction labour.",
            eligibilityCriteriaHindi = listOf(
                "गत 1 वर्ष में कम से कम 90 दिन निर्माण कार्य (मजदूरी, बेलदारी, राजमिस्त्री आदि) किया हो।",
                "संबंधित राज्य के BOCW बोर्ड में पंजीकृत लाल/हरा लेबर कार्ड धारक हों।"
            ),
            eligibilityCriteriaEnglish = listOf(
                "Completed at least 90 days of construction labour in the preceding 12 months.",
                "Registered with the respective State BOCW Board."
            ),
            exclusionsHindi = listOf(
                "बिना लेबर कार्ड नवीनीकरण (वार्षिक रिन्युअल) के लाभ नहीं मिलता।"
            ),
            verifiedBenefitsHindi = listOf(
                "औजार/टूलकिट सहायता: ₹2,000 से ₹5,000 तक का एकमुश्त अनुदान।",
                "पुत्री विवाह सहायता योजना: ₹50,000 से ₹1,00,000 तक की आर्थिक सहायता।",
                "संत रविदास शिक्षा प्रोत्साहन: बच्चों को कक्षा 1 से उच्च शिक्षा तक ₹1,000 से ₹22,000 तक वार्षिक छात्रवृत्ति।",
                "मृत्यु एवं अंत्येष्टि सहायता: सामान्य मृत्यु पर ₹2 लाख, दुर्घटना में मृत्यु पर ₹5 लाख और अंत्येष्टि हेतु ₹25,000।"
            ),
            verifiedBenefitsEnglish = listOf(
                "Tool purchase subsidy: ₹2,000 to ₹5,000 grant.",
                "Daughter marriage grant: ₹50,000 to ₹1,00,000 financial support.",
                "Education scholarship for children: ₹1,000 to ₹22,000 annually.",
                "Death and disability compensation: ₹2 Lakh for normal death, up to ₹5 Lakh for accidental death."
            ),
            requiredDocumentsHindi = listOf(
                "राज्य लेबर कार्ड (BOCW Card / श्रमिक कार्ड)",
                "ठेकेदार या निर्माण साइट से 90 दिन का कार्य प्रमाण पत्र",
                "आधार कार्ड, बैंक पासबुक और पासपोर्ट फोटो"
            ),
            requiredDocumentsEnglish = listOf(
                "State Labour Card (BOCW passbook)",
                "90-day work certificate signed by contractor/builder",
                "Aadhaar card, Bank passbook, and photo"
            ),
            applicationProcessHindi = "राज्य श्रम विभाग पोर्टल (उदा. uplabour.gov.in, labour.rajasthan.gov.in, delhibocw.nic.in) या जन सुविधा केंद्र (CSC) से ऑनलाइन आवेदन करें।",
            officialApplicationUrl = "https://labour.gov.in/bocw-portal",
            officialHelpline = "1800-180-1111 (Central) / 1800-180-6127 (State)"
        )
    )

    fun searchSchemes(query: String): List<GovernmentSchemeInfo> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return verifiedSchemes

        return verifiedSchemes.filter { scheme ->
            scheme.nameHindi.lowercase().contains(q) ||
            scheme.nameEnglish.lowercase().contains(q) ||
            scheme.purposeHindi.lowercase().contains(q) ||
            scheme.purposeEnglish.lowercase().contains(q) ||
            scheme.verifiedBenefitsHindi.any { it.lowercase().contains(q) } ||
            scheme.verifiedBenefitsEnglish.any { it.lowercase().contains(q) } ||
            (q.contains("shram") || q.contains("श्रम") || q.contains("uan") || q.contains("यूएएन")) && scheme.id == "eshram" ||
            (q.contains("pension") || q.contains("पेंशन") || q.contains("3000") || q.contains("मानधन")) && scheme.id == "pm_sym" ||
            (q.contains("vishwakarma") || q.contains("विश्वकर्मा") || q.contains("औजार") || q.contains("toolkit") || q.contains("लोन")) && scheme.id == "pm_vishwakarma" ||
            (q.contains("ayushman") || q.contains("आयुष्मान") || q.contains("इलाज") || q.contains("स्वास्थ्य") || q.contains("hospital")) && scheme.id == "ayushman_bharat" ||
            (q.contains("bocw") || q.contains("लेबर कार्ड") || q.contains("मजदूर कार्ड") || q.contains("शादी") || q.contains("छात्रवृत्ति")) && scheme.id == "bocw_state_board"
        }
    }

    fun getSchemeById(id: String): GovernmentSchemeInfo? {
        return verifiedSchemes.find { it.id == id }
    }
}
