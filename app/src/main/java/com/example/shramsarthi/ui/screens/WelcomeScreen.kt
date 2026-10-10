package com.example.shramsarthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.ui.components.BrandLogoBadge
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.components.TrustBadgePill
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryDark
import com.example.shramsarthi.ui.theme.TealPrimaryLight
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary

/**
 * Modern Welcome & Onboarding Screen for Shram Sarthi.
 * Visual hierarchy inspired by leading Indian marketplace apps (Blinkit, Rapido, Uber).
 * Deep forest teal primary branding, saffron accents, verified trust pillars, and clear CTAs.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit = onGetStartedClick,
    onExploreRolesClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val isLoggedIn = remember { ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9).copy(alpha = 0.5f),
                        BackgroundLight,
                        Color(0xFFFAFBFD)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val language = com.example.shramsarthi.util.LocalAppLanguage.current
            val isHindi = language == com.example.shramsarthi.util.AppLanguage.HINDI
            val context = androidx.compose.ui.platform.LocalContext.current

            Spacer(modifier = Modifier.height(8.dp))

            // Top Row: Indian Flag Mission badge + Language Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indian Flag & Mission badge
                Row(
                    modifier = Modifier
                        .background(
                            color = SaffronContainer.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = SaffronAccent.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(SaffronAccent, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "भारत का विश्वसनीय श्रम मंच" else "India's Fair Labor Network",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF78350F)
                    )
                }

                // Language Toggle Pill
                Box(
                    modifier = Modifier
                        .background(TealContainer, RoundedCornerShape(50.dp))
                        .border(1.dp, TealPrimary.copy(alpha = 0.3f), RoundedCornerShape(50.dp))
                        .clickable {
                            val newLang = if (isHindi) com.example.shramsarthi.util.AppLanguage.ENGLISH else com.example.shramsarthi.util.AppLanguage.HINDI
                            com.example.shramsarthi.util.LanguageManager.setLanguage(context, newLang)
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (isHindi) "English में बदलें ›" else "हिन्दी में बदलें ›",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Brand Logo & Emblem
            BrandLogoBadge(size = 84)

            Spacer(modifier = Modifier.height(18.dp))

            // Brand Title
            Text(
                text = "SHRAM SARTHI",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                color = TealPrimaryDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Vernacular Tagline
            Text(
                text = "काम • भरोसा • सहायता",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SaffronAccent,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Value proposition
            Text(
                text = "The unified digital marketplace connecting India's skilled workforce, contractors, builders, and everyday households with verified daily jobs and fair wages.",
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Ecosystem Role Preview (4 clickable cards)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ONE PLATFORM • FOUR PERSONAS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Explore All →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onExploreRolesClick() }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2x2 Grid of Roles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RoleMiniBadge(
                            role = UserRole.WORKER,
                            subtitle = "Find daily jobs & fair rates",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExploreRolesClick() }
                        )
                        RoleMiniBadge(
                            role = UserRole.THEKEDAR,
                            subtitle = "Hire labor & track sites",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExploreRolesClick() }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RoleMiniBadge(
                            role = UserRole.BUILDER,
                            subtitle = "Multi-site project labor",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExploreRolesClick() }
                        )
                        RoleMiniBadge(
                            role = UserRole.CUSTOMER,
                            subtitle = "Book home repair services",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExploreRolesClick() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Trust Badges Pill Row
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrustBadgePill(
                    text = "Aadhaar & Phone Verified",
                    icon = ShramIcons.CheckCircle,
                    tintColor = TealPrimary,
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                TrustBadgePill(
                    text = "Daily Fair Wage Standard",
                    icon = ShramIcons.CurrencyRupee,
                    tintColor = SaffronAccent,
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                TrustBadgePill(
                    text = "24/7 Shramik Helpline 14434",
                    icon = ShramIcons.Support,
                    tintColor = Color(0xFF2563EB),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Buttons
            if (isLoggedIn) {
                // If user already has an active Firebase session, show quick resume button
                ShramPrimaryButton(
                    text = "Continue to Dashboard →",
                    onClick = onGetStartedClick,
                    trailingIcon = ShramIcons.ArrowForward,
                    containerColor = TealPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Active Verified Session • Tap to resume",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            } else {
                // Primary CTA: Get Started
                ShramPrimaryButton(
                    text = if (isHindi) "शुरू करें" else "Get Started",
                    onClick = onGetStartedClick,
                    trailingIcon = ShramIcons.ArrowForward,
                    containerColor = TealPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary CTA: Log in for existing users
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onLoginClick() }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "पहले से खाता है? " else "Already have an account? ",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = if (isHindi) "लॉग इन करें" else "Log In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun RoleMiniBadge(
    role: UserRole,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = role.badgeColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = role.badgeColor.copy(alpha = 0.22f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(role.badgeColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = role.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = role.hindiTitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = role.badgeColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondary,
                lineHeight = 13.sp
            )
        }
    }
}
