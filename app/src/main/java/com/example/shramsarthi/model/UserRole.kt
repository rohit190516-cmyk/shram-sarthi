package com.example.shramsarthi.model

import androidx.compose.ui.graphics.Color
import com.example.shramsarthi.ui.theme.RoleBuilderColor
import com.example.shramsarthi.ui.theme.RoleCustomerColor
import com.example.shramsarthi.ui.theme.RoleThekedarColor
import com.example.shramsarthi.ui.theme.RoleWorkerColor

enum class UserRole(
    val title: String,
    val hindiTitle: String,
    val tagLine: String,
    val description: String,
    val badgeColor: Color,
    val keyFeatures: List<String>,
    val defaultSkills: List<String>
) {
    WORKER(
        title = "Worker",
        hindiTitle = "श्रमिक",
        tagLine = "Find work, verify skills, build trust.",
        description = "Find verified jobs daily, build a digital trust score, get voice assistance, and access government schemes.",
        badgeColor = RoleWorkerColor,
        keyFeatures = listOf(
            "AI Voice Job Search in Vernacular",
            "Skill Photo Verification",
            "Direct Fair Wage Payouts",
            "Emergency Assistance & Schemes"
        ),
        defaultSkills = listOf(
            "Mason / राजमिस्त्री",
            "Electrician / इलेक्ट्रीशियन",
            "Plumber / प्लंबर",
            "Carpenter / बढ़ई",
            "Painter / पेंटर",
            "Welder / वेल्डर",
            "Helper / सहायक"
        )
    ),
    THEKEDAR(
        title = "Thekedar",
        hindiTitle = "ठेकेदार",
        tagLine = "Post jobs and hire workers.",
        description = "Post requirements, source skilled worker crews rapidly, manage daily site attendance, and track rates.",
        badgeColor = RoleThekedarColor,
        keyFeatures = listOf(
            "Rapid Crew Job Posting",
            "Skill-based Applicant Screening",
            "Daily Site Attendance Tracker",
            "Transparent Wage Agreements"
        ),
        defaultSkills = listOf(
            "Civil Contractor / सिविल ठेकेदार",
            "Labor Contractor / लेबर सप्लायर",
            "Finishing Contractor / फिनिशिंग ठेकेदार",
            "Electrical Contractor / विद्युत ठेकेदार"
        )
    ),
    BUILDER(
        title = "Builder",
        hindiTitle = "बिल्डर",
        tagLine = "Manage workers, thekedars and active jobs.",
        description = "Scale multi-site labor supply, oversee contractor compliance, monitor trust scores, and streamline wage distribution.",
        badgeColor = RoleBuilderColor,
        keyFeatures = listOf(
            "Enterprise Multi-site Visibility",
            "Thekedar & Worker Trust Ratings",
            "Audit-ready Attendance Logs",
            "Standardized Payment Governance"
        ),
        defaultSkills = listOf(
            "Residential Projects / आवासीय निर्माण",
            "Commercial Projects / व्यावसायिक निर्माण",
            "Infrastructure / इंफ्रास्ट्रक्चर",
            "Renovation / नवीनीकरण"
        )
    ),
    CUSTOMER(
        title = "Customer",
        hindiTitle = "ग्राहक",
        tagLine = "Request services from nearby workers.",
        description = "Book verified plumbers, electricians, painters, and carpenters with fair suggested market pricing.",
        badgeColor = RoleCustomerColor,
        keyFeatures = listOf(
            "Pre-verified Local Technicians",
            "Transparent Market Rates",
            "Direct Contact & Quick Arrival",
            "Ratings & Trust Verification"
        ),
        defaultSkills = listOf(
            "Home Maintenance / घरेलू मरम्मत",
            "Renovation / घर नवीनीकरण",
            "New Construction / नया निर्माण",
            "Urgent Repair / आपातकालीन सेवा"
        )
    );

    fun getLocalizedTitle(isHindi: Boolean): String = if (isHindi) hindiTitle else title

    fun getLocalizedTagLine(isHindi: Boolean): String = when (this) {
        WORKER -> if (isHindi) "काम खोजें, कौशल सत्यापित करें, भरोसा बनाएं।" else "Find work, verify skills, build trust."
        THEKEDAR -> if (isHindi) "काम पोस्ट करें और कामगारों को जोड़ें।" else "Post jobs and hire workers."
        BUILDER -> if (isHindi) "साइट्स, ठेकेदार और परियोजनाएं प्रबंधित करें।" else "Manage sites, contractors, and active projects."
        CUSTOMER -> if (isHindi) "पास के कामगारों से सेवाएं लें।" else "Request services from nearby workers."
    }

    fun getLocalizedDescription(isHindi: Boolean): String = when (this) {
        WORKER -> if (isHindi) "रोज़ाना सत्यापित काम पाएं, डिजिटल ट्रस्ट स्कोर बनाएं, वॉयस सहायता लें और सरकारी योजनाओं का लाभ उठाएं।" else "Find verified jobs daily, build a digital trust score, get voice assistance, and access government schemes."
        THEKEDAR -> if (isHindi) "मांग दर्ज करें, कुशल कामगारों की टीम जोड़ें, दैनिक साइट हाज़िरी प्रबंधित करें और दरें ट्रैक करें।" else "Post requirements, source skilled worker crews rapidly, manage daily site attendance, and track rates."
        BUILDER -> if (isHindi) "मल्टी-साइट लेबर आपूर्ति देखें, ठेकेदार अनुपालन जांचें और पारदर्शी भुगतान सुनिश्चित करें।" else "Scale multi-site labor supply, oversee contractor compliance, monitor trust scores, and streamline wage distribution."
        CUSTOMER -> if (isHindi) "सत्यापित प्लंबर, इलेक्ट्रीशियन, पेंटर और बढ़ई को उचित बाज़ार दरों पर बुक करें।" else "Book verified plumbers, electricians, painters, and carpenters with fair suggested market pricing."
    }

    fun getLocalizedSkills(isHindi: Boolean): List<String> = when (this) {
        WORKER -> if (isHindi) listOf("राजमिस्त्री", "इलेक्ट्रीशियन", "प्लंबर", "बढ़ई", "पेंटर", "वेल्डर", "सहायक")
                  else listOf("Mason", "Electrician", "Plumber", "Carpenter", "Painter", "Welder", "Helper")
        THEKEDAR -> if (isHindi) listOf("सिविल ठेकेदार", "लेबर सप्लायर", "फिनिशिंग ठेकेदार", "विद्युत ठेकेदार")
                    else listOf("Civil Contractor", "Labor Contractor", "Finishing Contractor", "Electrical Contractor")
        BUILDER -> if (isHindi) listOf("आवासीय निर्माण", "व्यावसायिक निर्माण", "इंफ्रास्ट्रक्चर", "नवीनीकरण")
                   else listOf("Residential Projects", "Commercial Projects", "Infrastructure", "Renovation")
        CUSTOMER -> if (isHindi) listOf("घरेलू मरम्मत", "घर नवीनीकरण", "नया निर्माण", "आपातकालीन सेवा")
                    else listOf("Home Maintenance", "Renovation", "New Construction", "Urgent Repair")
    }

    fun getLocalizedKeyFeatures(isHindi: Boolean): List<String> = when (this) {
        WORKER -> if (isHindi) listOf(
            "मातृभाषा में AI वॉयस काम खोज",
            "कौशल फोटो सत्यापन",
            "सीधा व उचित मजदूरी भुगतान",
            "आपातकालीन सहायता एवं सरकारी योजनाएं"
        ) else keyFeatures
        THEKEDAR -> if (isHindi) listOf(
            "त्वरित कामगार मांग पोस्टिंग",
            "कौशल आधारित कामगार चयन",
            "दैनिक साइट हाज़िरी ट्रैकर",
            "पारदर्शी मजदूरी समझौते"
        ) else keyFeatures
        BUILDER -> if (isHindi) listOf(
            "मल्टी-साइट रियल टाइम दृश्यता",
            "ठेकेदार एवं कामगार ट्रस्ट रेटिंग",
            "ऑडिट-सक्षम हाज़िरी रिकॉर्ड",
            "मानकीकृत भुगतान व्यवस्था"
        ) else keyFeatures
        CUSTOMER -> if (isHindi) listOf(
            "सत्यापित स्थानीय तकनीशियन",
            "पारदर्शी मानक दरें",
            "सीधा संपर्क व त्वरित आगमन",
            "रेटिंग एवं विश्वास सत्यापन"
        ) else keyFeatures
    }

    companion object {
        fun getLocalizedSkill(skill: String, isHindi: Boolean): String =
            com.example.shramsarthi.model.getLocalizedSkill(skill, isHindi)
    }
}

/**
 * Normalizes any skill string (bilingual, English, or Hindi) into a pure localized string
 * based on the active language.
 */
fun getLocalizedSkill(skill: String, isHindi: Boolean): String {
    val clean = skill.trim()
    if (clean.isBlank()) return ""

    if (clean.contains("/")) {
        val parts = clean.split("/").map { it.trim() }
        if (parts.size >= 2) {
            val partEn = parts[0]
            val partHi = parts[1]
            return if (isHindi) {
                // If partHi has devanagari characters, use it; otherwise partEn
                if (partHi.any { it in '\u0900'..'\u097F' }) partHi else partEn
            } else {
                if (partEn.any { it in 'a'..'z' || it in 'A'..'Z' }) partEn else partHi
            }
        }
    }

    return when (clean) {
        "Mason", "राजमिस्त्री" -> if (isHindi) "राजमिस्त्री" else "Mason"
        "Electrician", "इलेक्ट्रीशियन" -> if (isHindi) "इलेक्ट्रीशियन" else "Electrician"
        "Plumber", "प्लंबर" -> if (isHindi) "प्लंबर" else "Plumber"
        "Carpenter", "बढ़ई" -> if (isHindi) "बढ़ई" else "Carpenter"
        "Painter", "पेंटर" -> if (isHindi) "पेंटर" else "Painter"
        "Welder", "वेल्डर" -> if (isHindi) "वेल्डर" else "Welder"
        "Helper", "सहायक" -> if (isHindi) "सहायक" else "Helper"
        "Civil Contractor", "सिविल ठेकेदार" -> if (isHindi) "सिविल ठेकेदार" else "Civil Contractor"
        "Labor Contractor", "लेबर सप्लायर" -> if (isHindi) "लेबर सप्लायर" else "Labor Contractor"
        "Finishing Contractor", "फिनिशिंग ठेकेदार" -> if (isHindi) "फिनिशिंग ठेकेदार" else "Finishing Contractor"
        "Electrical Contractor", "विद्युत ठेकेदार" -> if (isHindi) "विद्युत ठेकेदार" else "Electrical Contractor"
        "Residential Projects", "आवासीय निर्माण" -> if (isHindi) "आवासीय निर्माण" else "Residential Projects"
        "Commercial Projects", "व्यावसायिक निर्माण" -> if (isHindi) "व्यावसायिक निर्माण" else "Commercial Projects"
        "Infrastructure", "इंफ्रास्ट्रक्चर" -> if (isHindi) "इंफ्रास्ट्रक्चर" else "Infrastructure"
        "Renovation", "नवीनीकरण" -> if (isHindi) "नवीनीकरण" else "Renovation"
        "Home Maintenance", "घरेलू मरम्मत" -> if (isHindi) "घरेलू मरम्मत" else "Home Maintenance"
        "New Construction", "नया निर्माण" -> if (isHindi) "नया निर्माण" else "New Construction"
        "Urgent Repair", "आपातकालीन सेवा" -> if (isHindi) "आपातकालीन सेवा" else "Urgent Repair"
        else -> clean
    }
}

