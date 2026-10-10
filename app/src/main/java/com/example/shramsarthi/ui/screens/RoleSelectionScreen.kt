package com.example.shramsarthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.ui.components.BrandLogoBadge
import com.example.shramsarthi.ui.components.RoleSelectionCard
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryDark
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage

/**
 * Modern Role Selection Screen for Shram Sarthi.
 * Purely localized based on active language (Hindi or English).
 */
@Composable
fun RoleSelectionScreen(
    onNavigateBack: () -> Unit,
    onRoleSelected: (UserRole) -> Unit,
    initialRole: UserRole = UserRole.WORKER
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI

    var selectedRole by remember { mutableStateOf(initialRole) }
    val scrollState = rememberScrollState()
    val isLoggedIn = ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, BorderSubtle, CircleShape)
                    ) {
                        Icon(
                            imageVector = ShramIcons.ArrowBack,
                            contentDescription = if (isHindi) "पीछे जाएं" else "Back",
                            tint = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    BrandLogoBadge(size = 36)

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (isHindi) "भूमिका का चयन" else "Select Persona",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isHindi) "श्रम सारथी" else "Shram Sarthi",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Auth status indicator pill
                Box(
                    modifier = Modifier
                        .background(
                            if (isLoggedIn) TealPrimary.copy(alpha = 0.1f) else SaffronContainer,
                            RoundedCornerShape(20.dp)
                        )
                        .border(
                            1.dp,
                            if (isLoggedIn) TealPrimary.copy(alpha = 0.3f) else SaffronAccent.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isLoggedIn) ShramIcons.CheckCircle else ShramIcons.Info,
                            contentDescription = null,
                            tint = if (isLoggedIn) TealPrimary else SaffronAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isLoggedIn) {
                                if (isHindi) "सत्यापित सत्र" else "Verified Session"
                            } else {
                                if (isHindi) "पूर्वावलोकन" else "Preview"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isLoggedIn) TealPrimary else Color(0xFF78350F)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isHindi) "आपकी भूमिका क्या है?" else "Who are you?",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TealPrimaryDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isHindi) "श्रम सारथी आपके दैनिक काम और जिम्मेदारियों के अनुसार विशेष सुविधाएं देता है।" else "Shram Sarthi customizes tools, contracts, and services tailored to your daily operations.",
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 4 Role Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                UserRole.entries.forEach { role ->
                    RoleSelectionCard(
                        role = role,
                        isSelected = selectedRole == role,
                        onClick = { selectedRole = role }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dynamic Context Callout based on selected role
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(selectedRole.badgeColor.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .border(1.dp, selectedRole.badgeColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(selectedRole.badgeColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${selectedRole.getLocalizedTitle(isHindi)} ${if (isHindi) "विशेष सुविधाएं" else "Experience"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedRole.badgeColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val roleHighlights = if (isHindi) {
                        when (selectedRole) {
                            UserRole.WORKER -> "• प्रत्यक्ष ठेकेदारों से दैनिक मजदूरी के सत्यापित काम पाएं\n• उचित मजदूरी गारंटी और 24/7 आपातकालीन हेल्पलाइन\n• मातृभाषा में AI वॉयस सहायक"
                            UserRole.THEKEDAR -> "• मिनटों में कामगारों की मांग दर्ज करें व टीम जोड़ें\n• दैनिक साइट हाज़िरी और सत्यापित कामगार प्रोफ़ाइल\n• आसान डिजिटल भुगतान और टीम प्रबंधन"
                            UserRole.BUILDER -> "• मल्टी-साइट निर्माण अनुबंध और लेबर आपूर्ति निगरानी\n• ठेकेदार अनुपालन और रियल टाइम साइट दृश्यता\n• मानकीकृत भुगतान और ऑडिट रिकॉर्ड"
                            UserRole.CUSTOMER -> "• सत्यापित पेंटर, इलेक्ट्रीशियन, प्लंबर व राजमिस्त्री बुक करें\n• बिना मोलभाव पारदर्शी मानक दरें\n• स्थानीय कुशल तकनीशियनों से सीधा संपर्क"
                        }
                    } else {
                        when (selectedRole) {
                            UserRole.WORKER -> "• Access verified daily wage jobs from direct contractors\n• Fair Wage Guarantee & instant emergency hotline\n• Voice assistant in Hindi & English"
                            UserRole.THEKEDAR -> "• Publish labor demand & recruit skilled crews in minutes\n• Track daily site attendance & verified worker profiles\n• Fast digital payments and crew management"
                            UserRole.BUILDER -> "• Multi-site construction contract oversight\n• Large-scale labor contractor sourcing & compliance\n• Real-time site status & workforce analytics"
                            UserRole.CUSTOMER -> "• Find verified painters, electricians, plumbers & masons\n• Standard transparent rate cards without haggling\n• Direct contact with certified local technicians"
                        }
                    }

                    Text(
                        text = roleHighlights,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )

                    if (!isLoggedIn) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isHindi) "नोट: इस डैशबोर्ड में जाने के लिए मोबाइल नंबर का ओटीपी सत्यापन आवश्यक है।" else "Note: You will be asked to verify your phone number via OTP to access this dashboard.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = SaffronAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sticky Action Button
            ShramPrimaryButton(
                text = if (isLoggedIn) {
                    if (isHindi) "${selectedRole.hindiTitle} डैशबोर्ड खोलें →" else "Enter ${selectedRole.title} Dashboard →"
                } else {
                    if (isHindi) "ओटीपी सत्यापित करें और आगे बढ़ें →" else "Verify Mobile & Continue as ${selectedRole.title} →"
                },
                onClick = { onRoleSelected(selectedRole) },
                containerColor = selectedRole.badgeColor,
                trailingIcon = ShramIcons.ArrowForward
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
