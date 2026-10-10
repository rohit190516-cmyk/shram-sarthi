package com.example.shramsarthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.ui.components.BrandLogoBadge
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderStrong
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryDark
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary

/**
 * Dedicated Terms & Conditions and Privacy Notice screen.
 * Covers all 12 platform terms sections and clear privacy disclosure.
 * Clarifies marketplace connector role without false guarantees.
 */
@Composable
fun TermsAndConditionsScreen(
    onNavigateBack: () -> Unit
) {
    val isHindi = LocalAppLanguage.current == AppLanguage.HINDI
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Terms, 1: Privacy
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TOP BAR
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFFF8FAFC), CircleShape)
                            .border(1.dp, BorderSubtle, CircleShape)
                    ) {
                        Icon(
                            imageVector = ShramIcons.ArrowBack,
                            contentDescription = if (isHindi) "वापस" else "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    BrandLogoBadge(size = 36)

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) "नियम व शर्तें" else "Terms & Policy",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isHindi) "श्रम सारथी मंच दिशानिर्देश" else "Shram Sarthi Platform Guidelines",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // TAB SELECTOR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTab == 0) TealPrimary else Color.Transparent)
                        .clickable { selectedTab = 0 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isHindi) "12 सेवा शर्तें" else "12 Service Terms",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) Color.White else TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTab == 1) TealPrimary else Color.Transparent)
                        .clickable { selectedTab = 1 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isHindi) "गोपनीयता नीति" else "Privacy Policy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) Color.White else TextSecondary
                    )
                }
            }

            // SCROLLABLE CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 18.dp, vertical = 4.dp)
            ) {
                if (selectedTab == 0) {
                    // --- TERMS OF SERVICE TAB ---

                    // Essential Marketplace Disclosure Box
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SaffronContainer.copy(alpha = 0.5f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SaffronAccent.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ShramIcons.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isHindi) "मंच की भूमिका एवं अस्वीकरण" else "Platform Role & Disclaimer",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isHindi) {
                                    "श्रम सारथी केवल कामगारों, ठेकेदारों, बिल्डरों एवं ग्राहकों को आपस में जोड़ने का साधन है।\n• मंच किसी भी रोजगार, मजदूरी या भुगतान की गारंटी नहीं देता है।\n• मंच प्रत्येक उपयोगकर्ता अथवा कार्य की व्यक्तिगत पृष्ठभूमि सत्यापन का दावा नहीं करता है। सभी समझौते दोनों पक्षों के बीच प्रत्यक्ष होते हैं।"
                                } else {
                                    "Shram Sarthi is a direct connection marketplace connecting workers, contractors, builders, and customers.\n• The platform does not guarantee employment, wages, or payment.\n• The platform does not claim background verification of every user or job. All agreements are direct between the respective parties."
                                },
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = Color(0xFF92400E)
                            )
                        }
                    }

                    // 12 Dedicated Sections
                    TermsSectionCard(
                        number = "1",
                        title = if (isHindi) "स्वीकृति एवं पात्रता" else "Acceptance & Eligibility",
                        content = if (isHindi) {
                            "श्रम सारथी ऐप का उपयोग करके आप इन नियमों को स्वीकार करते हैं। खाता बनाने के लिए उपयोगकर्ता की आयु कम से कम 18 वर्ष होनी चाहिए और कानूनी रूप से अनुबंध करने में सक्षम होना चाहिए।"
                        } else {
                            "By using Shram Sarthi, you agree to these terms. Users must be at least 18 years of age and legally competent to enter into binding agreements."
                        }
                    )

                    TermsSectionCard(
                        number = "2",
                        title = if (isHindi) "खाता एवं ओटीपी सुरक्षा" else "Account & OTP Security",
                        content = if (isHindi) {
                            "आपका खाता आपके सत्यापित फोन नंबर से बंधा है। आपके नंबर पर प्राप्त ओटीपी गोपनीय है। अपना ओटीपी कभी किसी के साथ साझा न करें। आपके खाते से होने वाली किसी भी गतिविधि के लिए आप स्वयं जिम्मेदार हैं।"
                        } else {
                            "Your account is linked to your verified phone number. OTPs received on your phone are confidential. Never share OTPs. You are responsible for all actions conducted under your account."
                        }
                    )

                    TermsSectionCard(
                        number = "3",
                        title = if (isHindi) "सत्य पंजीकरण विवरण" else "Accurate Registration Details",
                        content = if (isHindi) {
                            "पंजीकरण के समय अपना वास्तविक नाम, वैध फोन नंबर, वास्तविक शहर/क्षेत्र और सही कौशल/भूमिका दर्ज करें। भ्रामक, गलत या अन्य व्यक्ति की पहचान का उपयोग करना प्रतिबंधित है।"
                        } else {
                            "Enter your authentic legal name, valid phone number, genuine locality, and true trade skills. Misrepresenting identity or credentials is strictly prohibited."
                        }
                    )

                    TermsSectionCard(
                        number = "4",
                        title = if (isHindi) "कारीगर प्रोफ़ाइल व कार्य आवेदन" else "Worker Profiles & Applications",
                        content = if (isHindi) {
                            "कारीगर अपनी वास्तविक विशेषज्ञता और अनुभव के अनुसार काम के लिए आवेदन करें। कार्य स्थल पर समय की पाबंदी और व्यावसायिक आचरण बनाए रखना आवश्यक है।"
                        } else {
                            "Workers must apply only for jobs matching their actual skills and experience. Punctuality and professional conduct on worksites are mandatory."
                        }
                    )

                    TermsSectionCard(
                        number = "5",
                        title = if (isHindi) "नियोक्ता की जिम्मेदारी" else "Hiring Responsibilities",
                        content = if (isHindi) {
                            "कार्य पोस्ट करने वाले नियोक्ताओं को कार्य की प्रकृति, स्थल, आवश्यकता और प्रस्तावित पारिश्रमिक का स्पष्ट विवरण देना होगा। कार्य स्थल पर सुरक्षा मानकों का पालन नियोक्ता की जिम्मेदारी है।"
                        } else {
                            "Employers posting requisitions must clearly state the job scope, location, daily wage, and requirements. Site safety and regulatory compliance are the employer's responsibility."
                        }
                    )

                    TermsSectionCard(
                        number = "6",
                        title = if (isHindi) "कार्य उपलब्धता, मजदूरी व भुगतान" else "Wages, Payments & Disputes",
                        content = if (isHindi) {
                            "श्रम सारथी मजदूरी का प्रत्यक्ष भुगतान या संग्रह नहीं करता। पारिश्रमिक और भुगतान का माध्यम कारीगर और नियोक्ता के बीच आपसी सहमति पर निर्भर है। भुगतान संबंधी विवादों को दोनों पक्ष आपसी बातचीत से सुलझाएंगे; मंच इसमें मध्यस्थ या पक्षकार नहीं है।"
                        } else {
                            "Shram Sarthi does not hold or distribute worker wages directly. Remuneration is agreed upon between worker and employer directly. Disputes are resolved bilaterally without platform liability."
                        }
                    )

                    TermsSectionCard(
                        number = "7",
                        title = if (isHindi) "निषिद्ध आचरण व धोखाधड़ी" else "Prohibited Conduct & Anti-Fraud",
                        content = if (isHindi) {
                            "नकली जॉब पोस्ट करना, गैर-कानूनी कार्य कराना, उत्पीड़न, गाली-गलौज, बिचौलियों द्वारा शोषण अथवा उपयोगकर्ताओं को धोखा देना सख्त मना है। ऐसे मामलों में तत्काल कार्रवाई की जाएगी।"
                        } else {
                            "Posting fraudulent jobs, illegal assignments, harassment, predatory middlemen exploitation, and deceptive behavior are strictly barred."
                        }
                    )

                    TermsSectionCard(
                        number = "8",
                        title = if (isHindi) "स्थान व व्यक्तिगत डेटा उपयोग" else "Location & Personal Data",
                        content = if (isHindi) {
                            "उपयुक्त कार्य और निकटतम कामगारों की खोज के लिए आपका शहर/क्षेत्र और संपर्क नंबर उपयोग किया जाता है। व्यक्तिगत डेटा को किसी तीसरे पक्ष को विपणन के लिए नहीं बेचा जाता।"
                        } else {
                            "Location and verified contact numbers are used solely to match you with nearby work opportunities. Personal data is never sold to third-party advertisers."
                        }
                    )

                    TermsSectionCard(
                        number = "9",
                        title = if (isHindi) "सूचनाएं व संदेश" else "Notifications & Communications",
                        content = if (isHindi) {
                            "कार्य अपडेट, आवेदन स्थिति, सुरक्षा अलर्ट एवं सेवा संदेश आपको एसएमएस अथवा ऐप सूचनाओं के माध्यम से भेजे जा सकते हैं।"
                        } else {
                            "Job status updates, work notifications, and critical platform alerts may be delivered via SMS and app push notifications."
                        }
                    )

                    TermsSectionCard(
                        number = "10",
                        title = if (isHindi) "सेवा सीमाएं व देयता" else "Service Limitations & Liability",
                        content = if (isHindi) {
                            "श्रम सारथी ऐप को 'जैसा है' (As-is) आधार पर प्रदान करता है। किसी भी अप्रत्यक्ष नुकसान, कार्य स्थल पर दुर्घटना, वित्तीय हानि या अन्य उपयोगकर्ता के व्यक्तिगत आचरण के लिए मंच जिम्मेदार नहीं होगा।"
                        } else {
                            "The platform is provided on an 'as-is' basis. Shram Sarthi is not liable for indirect damages, worksite accidents, financial loss, or conduct of third parties."
                        }
                    )

                    TermsSectionCard(
                        number = "11",
                        title = if (isHindi) "खाता निलंबन व समाप्ति" else "Account Suspension & Termination",
                        content = if (isHindi) {
                            "नियमों के उल्लंघन, धोखाधड़ी, भ्रामक विवरण या अन्य उपयोगकर्ताओं की सुरक्षा को खतरे में डालने पर बिना पूर्व सूचना के खाता निलंबित या बंद किया जा सकता है।"
                        } else {
                            "Accounts that violate safety guidelines, engage in fraud, or provide false registration details may be suspended without prior notice."
                        }
                    )

                    TermsSectionCard(
                        number = "12",
                        title = if (isHindi) "शर्तों में बदलाव व सहायता" else "Changes & Support Contact",
                        content = if (isHindi) {
                            "मंच आवश्यकतानुसार इन शर्तों को अपडेट कर सकता है। किसी भी सहायता या प्रश्न के लिए हमारे हेल्पलाइन 14434 अथवा support@shramsarthi.in पर संपर्क करें।"
                        } else {
                            "These terms may be updated periodically. For questions or support, contact our helpline at 14434 or email support@shramsarthi.in."
                        }
                    )

                } else {
                    // --- PRIVACY NOTICE TAB ---

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = TealContainer.copy(alpha = 0.4f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "गोपनीयता सूचना" else "Privacy Notice",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) {
                                    "हम आपकी निजता का सम्मान करते हैं। नीचे विवरण दिया गया है कि हम कौन-सा डेटा एकत्र करते हैं और उसका उपयोग कैसे किया जाता है।"
                                } else {
                                    "We respect your privacy. Below is an overview of what information is collected and how it is protected."
                                },
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    PrivacyItemCard(
                        title = if (isHindi) "1. हम क्या डेटा एकत्र करते हैं?" else "1. What Data We Collect",
                        description = if (isHindi) {
                            "• सत्यापित मोबाइल नंबर (Firebase फोन प्रमाणीकरण द्वारा)\n• आपका पूरा नाम\n• शहर या इलाका (स्थान खोज के लिए)\n• चुनी गई भूमिका (कारीगर, ठेकेदार, बिल्डर या ग्राहक)\n• मुख्य कौशल या कार्य श्रेणी"
                        } else {
                            "• Verified mobile number (via Firebase Phone Auth)\n• Full legal name\n• City or locality (for proximity job matching)\n• Selected persona role (Worker, Contractor, Builder, Customer)\n• Primary trade skills"
                        }
                    )

                    PrivacyItemCard(
                        title = if (isHindi) "2. हम इसका उपयोग क्यों करते हैं?" else "2. Why We Use It",
                        description = if (isHindi) {
                            "• आपके खाते में सुरक्षित लॉगिन सुनिश्चित करने के लिए\n• कारीगरों को उनके कौशल और क्षेत्र के अनुसार कार्य से जोड़ने के लिए\n• ठेकेदारों और ग्राहकों को कुशल कारीगर खोजने में सहायता के लिए"
                        } else {
                            "• To verify and secure your account\n• To connect workers with relevant local work opportunities\n• To assist contractors, builders, and customers in hiring verified talent"
                        }
                    )

                    PrivacyItemCard(
                        title = if (isHindi) "3. फोन प्रमाणीकरण कैसे कार्य करता है?" else "3. How Phone Authentication Works",
                        description = if (isHindi) {
                            "लॉगिन और खाता सुरक्षा के लिए हम Google Firebase Authentication का उपयोग करते हैं। जब आप अपना नंबर दर्ज करते हैं, तो 6-अंकीय सुरक्षित एसएमएस ओटीपी भेजा जाता है। पासवर्ड याद रखने की आवश्यकता नहीं होती।"
                        } else {
                            "We use Google Firebase Authentication for frictionless login. When you enter your phone number, a 6-digit SMS OTP is sent. No password management is needed."
                        }
                    )

                    PrivacyItemCard(
                        title = if (isHindi) "4. स्थान व प्रोफ़ाइल डेटा की सुरक्षा" else "4. Location & Profile Security",
                        description = if (isHindi) {
                            "हम केवल आपके द्वारा दर्ज शहर/इलाके का उपयोग निकटतम कार्य प्रदर्शित करने के लिए करते हैं। आपकी व्यक्तिगत जानकारी को व्यावसायिक विज्ञापनों के लिए तीसरे पक्षों को नहीं बेचा जाता।"
                        } else {
                            "We use your registered locality solely to display nearby job postings. Your information is never sold to third parties for advertising."
                        }
                    )

                    PrivacyItemCard(
                        title = if (isHindi) "5. डेटा सहायता व अनुरोध" else "5. Data Rights & Assistance",
                        description = if (isHindi) {
                            "यदि आप अपनी प्रोफ़ाइल में सुधार करना चाहते हैं या खाता हटाने का अनुरोध करना चाहते हैं, तो आप ऐप के भीतर प्रोफ़ाइल अपडेट कर सकते हैं या support@shramsarthi.in पर सहायता टीम से संपर्क कर सकते हैं।"
                        } else {
                            "To request profile updates or account deletion, manage your in-app profile settings or reach our support team at support@shramsarthi.in."
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Acknowledgment CTA
                ShramPrimaryButton(
                    text = if (isHindi) "समझ लिया" else "I Understand",
                    onClick = onNavigateBack,
                    modifier = Modifier.height(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TermsSectionCard(
    number: String,
    title: String,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(TealContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun PrivacyItemCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )
        }
    }
}
