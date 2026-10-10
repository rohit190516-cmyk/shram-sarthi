package com.example.shramsarthi.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.shramsarthi.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobStatus
import com.example.shramsarthi.model.RateType
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.model.getLocalizedSkill
import com.example.shramsarthi.ui.theme.BorderStrong
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.SaffronLight
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryLight
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage

/**
 * Premium primary button with rounded corners and optional leading/trailing icons and loading state.
 */
@Composable
fun ShramPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingText: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    containerColor: Color = TealPrimary
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = loadingText ?: if (isHindi) "सहेजा जा रहा है..." else "Processing...",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Premium secondary/outlined button.
 */
@Composable
fun ShramOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    borderColor: Color = TealPrimary
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = borderColor
        ),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = borderColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Modern styled text input field.
 */
@Composable
fun ShramTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontSize = 14.sp) },
            placeholder = { Text(placeholder, fontSize = 14.sp, color = TextMuted) },
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            visualTransformation = visualTransformation,
            isError = isError,
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) Color(0xFFDC2626) else TealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealPrimary,
                unfocusedBorderColor = BorderStrong,
                focusedLabelColor = TealPrimary,
                cursorColor = TealPrimary,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFFAFCFD)
            )
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color(0xFFDC2626),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Official Brand Logo Badge for Shram Sarthi with original colors and proportions.
 */
@Composable
fun BrandLogoBadge(
    modifier: Modifier = Modifier,
    size: Int = 80
) {
    Image(
        painter = painterResource(id = R.drawable.shram_sarthi_logo),
        contentDescription = "Shram Sarthi logo",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
    )
}


/**
 * Trust Badge Item for credibility points.
 */
@Composable
fun TrustBadgePill(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    tintColor: Color = TealPrimary
) {
    Row(
        modifier = modifier
            .background(
                color = tintColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(50.dp)
            )
            .border(
                width = 1.dp,
                color = tintColor.copy(alpha = 0.25f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tintColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = tintColor
        )
    }
}

/**
 * Interactive Role Selection Card with distinct role badge, vernacular title,
 * and key feature points.
 */
@Composable
fun RoleSelectionCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) role.badgeColor else BorderSubtle,
        label = "roleBorderColor"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) role.badgeColor.copy(alpha = 0.05f) else Color.White,
        label = "roleBgColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Role Emblem circle
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = role.badgeColor.copy(alpha = 0.15f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = role.title.take(1),
                            color = role.badgeColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    val language = LocalAppLanguage.current
                    val isHindi = language == AppLanguage.HINDI

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = role.getLocalizedTitle(isHindi),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = role.getLocalizedTagLine(isHindi),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) role.badgeColor else TextSecondary
                        )
                    }
                }

                // Radio / Check Indicator
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (isSelected) role.badgeColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) role.badgeColor else BorderStrong,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = ShramIcons.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val language = LocalAppLanguage.current
            val isHindi = language == AppLanguage.HINDI

            Text(
                text = role.getLocalizedDescription(isHindi),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Features chips preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                role.getLocalizedKeyFeatures(isHindi).take(2).forEach { feature ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) role.badgeColor.copy(alpha = 0.1f) else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = feature,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) role.badgeColor else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern placeholder card for upcoming Phase 3/4 capabilities.
 */
@Composable
fun DashboardPlaceholderCard(
    title: String,
    subtitle: String,
    description: String,
    tag: String = "Upcoming Phase",
    icon: ImageVector? = null,
    badgeColor: Color = TealPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (icon != null) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(badgeColor.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = badgeColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Column {
                        Text(
                            text = title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.12f), RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )
        }
    }
}

/**
 * Top header bar for role dashboards with app branding, role badge, back button, and logout button.
 */
@Composable
fun DashboardRoleHeader(
    role: UserRole,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, BorderSubtle, CircleShape)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ShramIcons.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandLogoBadge(size = 28)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SHRAM SARTHI",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TealPrimary
                    )
                }
                Text(
                    text = "${role.title} Portal • ${role.hindiTitle}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = role.badgeColor
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFEE2E2))
                .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(10.dp))
                .clickable { onLogoutClick() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ShramIcons.Logout,
                    contentDescription = "Logout",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Logout",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFDC2626)
                )
            }
        }
    }
}

/**
 * Modern consumer header bar (Rapido/Uber + Blinkit style)
 * Location pill, User Profile/Greeting, Emergency SOS badge, and Notification bell.
 */
@Composable
fun ShramTopHeader(
    userName: String,
    userRole: UserRole,
    location: String,
    onSosClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: User Greeting & Location Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(userRole.badgeColor.copy(alpha = 0.15f))
                        .border(1.5.dp, userRole.badgeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (userName.isNotBlank()) userName.take(1).uppercase() else userRole.title.take(1),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = userRole.badgeColor
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (userName.isNotBlank()) "नमस्ते, $userName" else "नमस्ते, ${userRole.hindiTitle}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .background(userRole.badgeColor.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = userRole.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = userRole.badgeColor
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = ShramIcons.LocationPin,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = location.ifBlank { "Jaipur, Rajasthan" },
                            fontSize = 12.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Right: Emergency SOS Action & Notifications
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Emergency SOS Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(20.dp))
                        .clickable { onSosClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ShramIcons.Emergency,
                            contentDescription = "SOS",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SOS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }
                }

                // Notification Bell
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFF8FAFC), CircleShape)
                        .border(1.dp, BorderSubtle, CircleShape)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = ShramIcons.Notifications,
                            contentDescription = "Notifications",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(Color(0xFFD97706), CircleShape)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern prominent Search Bar with voice mic and filter button.
 */
@Composable
fun ShramSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String? = null,
    onMicClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI
    val defaultPlaceholder = if (isHindi) "दैनिक काम, कौशल या स्थान खोजें..." else "Search daily jobs, skills, locations..."

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ShramIcons.Search,
                contentDescription = if (isHindi) "खोजें" else "Search",
                tint = TealPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = placeholder ?: defaultPlaceholder,
                        fontSize = 13.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            IconButton(onClick = onMicClick, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = ShramIcons.Mic,
                    contentDescription = if (isHindi) "बोलकर खोजें" else "Voice Search",
                    tint = SaffronAccent,
                    modifier = Modifier.size(19.dp)
                )
            }
            IconButton(onClick = onFilterClick, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = ShramIcons.FilterList,
                    contentDescription = if (isHindi) "फ़िल्टर" else "Filter",
                    tint = TextSecondary,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}

/**
 * Skill Categories horizontal carousel (Blinkit style pill tags).
 */
@Composable
fun ShramSkillFilterRow(
    skills: List<String>,
    selectedSkill: String?,
    onSkillSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp)
    ) {
        item {
            val isAllSelected = selectedSkill == null
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isAllSelected) TealPrimary else Color.White)
                    .border(
                        1.dp,
                        if (isAllSelected) TealPrimary else BorderStrong,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSkillSelected(null) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isHindi) "सभी कौशल" else "All Skills",
                    fontSize = 12.sp,
                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isAllSelected) Color.White else TextPrimary
                )
            }
        }

        items(skills) { skill ->
            val localizedSkill = getLocalizedSkill(skill, isHindi)
            val isSelected = selectedSkill == skill || selectedSkill == localizedSkill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) TealPrimary else Color.White)
                    .border(
                        1.dp,
                        if (isSelected) TealPrimary else BorderStrong,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSkillSelected(if (isSelected) null else skill) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedSkill,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else TextPrimary
                )
            }
        }
    }
}

/**
 * Modern Indian marketplace job card displaying real Firestore data.
 */
@Composable
fun ShramLiveJobCard(
    job: Job,
    onApplyClick: () -> Unit,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onViewDetailsClick() }
            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Title and Worker Type Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.title.ifBlank { "Construction Requirement" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = job.workerType.ifBlank { "General Karigar" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealPrimary
                    )
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .background(
                            when (job.status) {
                                JobStatus.OPEN -> Color(0xFFECFDF5)
                                JobStatus.IN_PROGRESS -> Color(0xFFEFF6FF)
                                JobStatus.COMPLETED -> Color(0xFFF1F5F9)
                                JobStatus.CANCELLED -> Color(0xFFFEF2F2)
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            when (job.status) {
                                JobStatus.OPEN -> Color(0xFFA7F3D0)
                                JobStatus.IN_PROGRESS -> Color(0xFFBFDBFE)
                                JobStatus.COMPLETED -> BorderSubtle
                                JobStatus.CANCELLED -> Color(0xFFFECACA)
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = job.status.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (job.status) {
                            JobStatus.OPEN -> Color(0xFF047857)
                            JobStatus.IN_PROGRESS -> Color(0xFF1D4ED8)
                            JobStatus.COMPLETED -> TextMuted
                            JobStatus.CANCELLED -> Color(0xFFDC2626)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metadata Row: Location & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ShramIcons.LocationPin,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = job.location.ifBlank { "Jaipur, Rajasthan" },
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = ShramIcons.Group,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                val language = LocalAppLanguage.current
                val isHindi = language == AppLanguage.HINDI

                Text(
                    text = if (isHindi) "${job.workersRequired} कामगार चाहिए" else "${job.workersRequired} Needed",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
            }

            if (job.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = job.description,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = TextMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BorderSubtle, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(12.dp))

            val language = LocalAppLanguage.current
            val isHindi = language == AppLanguage.HINDI

            // Footer: Rate & Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "दिहाड़ी दर" else "WAGE RATE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₹${job.rate.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TealPrimary
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isHindi) "/ दिन" else "/ day",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, BorderStrong, RoundedCornerShape(10.dp))
                            .clickable { onViewDetailsClick() }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isHindi) "विवरण" else "Details",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(TealPrimary)
                            .clickable { onApplyClick() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isHindi) "आवेदन करें" else "Apply Now",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern 4-Tab Bottom Navigation Bar (Home, Records, Complaints, Profile) matching approved UI.
 */
@Composable
fun ShramBottomNav(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = com.example.shramsarthi.util.LocalAppLanguage.current
    val isHindi = language == com.example.shramsarthi.util.AppLanguage.HINDI

    val items = listOf(
        Triple("home", if (isHindi) "होम" else "Home", ShramIcons.Home),
        Triple("records", if (isHindi) "रिकॉर्ड" else "Records", ShramIcons.Assignment),
        Triple("complaints", if (isHindi) "शिकायत" else "Complaints", ShramIcons.ShieldAlert),
        Triple("profile", if (isHindi) "प्रोफ़ाइल" else "Profile", ShramIcons.Person)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 8.dp,
        border = BorderStroke(0.5.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (tabKey, label, icon) ->
                val isSelected = selectedTab == tabKey || (tabKey == "records" && selectedTab == "my_jobs")
                val activeColor = Color(0xFF047857) // Dark rich green matching the references
                val inactiveColor = Color(0xFF64748B)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onTabSelected(tabKey) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top indicator line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(3.dp)
                            .background(
                                color = if (isSelected) activeColor else Color.Transparent,
                                shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)
                            )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeColor else inactiveColor,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

/**
 * Job Detail Modal Dialog with full wage details and one-tap application.
 */
@Composable
fun ShramJobDetailDialog(
    job: Job,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    isApplying: Boolean = false,
    hasApplied: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                val language = LocalAppLanguage.current
                val isHindi = language == AppLanguage.HINDI

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "कार्य विवरण" else "Job Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = ShramIcons.Close, contentDescription = if (isHindi) "बंद करें" else "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = job.title.ifBlank { if (isHindi) "काम की आवश्यकता" else "Requirement" },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${if (isHindi) "कौशल: " else "Category: "}${getLocalizedSkill(job.workerType, isHindi).ifBlank { if (isHindi) "सामान्य" else "General" }}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Rate Highlight Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFECFDF5), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = if (isHindi) "तय दिहाड़ी मजदूरी" else "Guaranteed Wage", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = "₹${job.rate.toInt()} ${if (isHindi) "/ दिन" else "/ day"}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF047857)
                            )
                        }
                        Text(
                            text = if (isHindi) "सीधा भुगतान" else "Direct Payout",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Info Rows
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ShramIcons.LocationPin, contentDescription = null, tint = SaffronAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "${if (isHindi) "स्थान: " else "Site: "}${job.location.ifBlank { if (isHindi) "जयपुर, राजस्थान" else "Jaipur, Rajasthan" }}", fontSize = 13.sp, color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ShramIcons.Group, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isHindi) "आवश्यकता: ${job.workersRequired} कामगार" else "Requirement: ${job.workersRequired} Workers", fontSize = 13.sp, color = TextPrimary)
                }

                if (job.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = if (isHindi) "विवरण:" else "Description:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = job.description, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (hasApplied) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFECFDF5), RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isHindi) "✓ आवेदन जमा किया गया" else "✓ Application Submitted",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        )
                    }
                } else {
                    ShramPrimaryButton(
                        text = if (isHindi) "आवेदन जमा करें" else "Submit Application",
                        onClick = onApply,
                        isLoading = isApplying,
                        loadingText = if (isHindi) "आवेदन भेजा जा रहा है..." else "Submitting...",
                        containerColor = TealPrimary
                    )
                }
            }
        }
    }
}

/**
 * Emergency SOS Modal Bottom Sheet with helplines.
 */
@Composable
fun ShramEmergencySheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ShramIcons.Emergency,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "आपातकालीन सहायता" else "Emergency Assistance",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(26.dp)) {
                        Icon(imageVector = ShramIcons.Close, contentDescription = if (isHindi) "बंद करें" else "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = if (isHindi) {
                        "यदि कार्य स्थल पर कोई दुर्घटना, मजदूरी विवाद या सुरक्षा समस्या हो, तो तत्काल सहायता के लिए नीचे टैप करें:"
                    } else {
                        "If you face an accident, wage dispute, or safety issue on a work site, tap below for immediate assistance:"
                    },
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Helpline 1: 112 National Emergency
                EmergencyDialRow(
                    title = if (isHindi) "राष्ट्रीय आपातकालीन सेवा" else "National Emergency",
                    number = "112",
                    hindiSubtitle = if (isHindi) "पुलिस, मेडिकल और अग्निशमन" else "Police, Medical & Fire",
                    onClick = {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                            data = android.net.Uri.parse("tel:112")
                        }
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Helpline 2: 14434 Shramik Helpline
                EmergencyDialRow(
                    title = if (isHindi) "श्रम मंत्रालय कामगार हेल्पलाइन" else "Ministry Worker Helpline",
                    number = "14434",
                    hindiSubtitle = if (isHindi) "अधिकार, वेतन व श्रमिक सहायता" else "Worker Rights & Assistance",
                    onClick = {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                            data = android.net.Uri.parse("tel:14434")
                        }
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Helpline 3: 1077 Disaster Helpline
                EmergencyDialRow(
                    title = if (isHindi) "आपदा व दुर्घटना सहायता" else "Disaster & Site Accident Helpline",
                    number = "1077",
                    hindiSubtitle = if (isHindi) "कार्यस्थल संकट एवं सुरक्षा" else "On-site Crisis & Safety",
                    onClick = {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                            data = android.net.Uri.parse("tel:1077")
                        }
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                ShramOutlinedButton(
                    text = if (isHindi) "बंद करें" else "Close",
                    onClick = onDismiss,
                    borderColor = BorderStrong
                )
            }
        }
    }
}

@Composable
private fun EmergencyDialRow(
    title: String,
    number: String,
    hindiSubtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEF2F2))
            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = hindiSubtitle, fontSize = 11.sp, color = TextSecondary)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFDC2626))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ShramIcons.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = number, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/**
 * Clean Empty State card with optional action button.
 */
@Composable
fun ShramEmptyState(
    title: String,
    subtitle: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(TealContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon ?: ShramIcons.Work,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (buttonText != null && onButtonClick != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(TealPrimary)
                        .clickable { onButtonClick() }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Dialog for Thekedars and Builders to post real job requirements.
 */
@Composable
fun ShramPostJobDialog(
    initialCategory: String = "राजमिस्त्री",
    onDismiss: () -> Unit,
    onPostJob: (Job) -> Unit
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI

    var title by remember { mutableStateOf("") }
    var workerType by remember { mutableStateOf(getLocalizedSkill(initialCategory, isHindi)) }
    var location by remember { mutableStateOf(if (isHindi) "ग्रेटर नोएडा" else "Greater Noida") }
    var rateText by remember { mutableStateOf("750") }
    var workersNeededText by remember { mutableStateOf("2") }
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "नया काम पोस्ट करें" else "Post New Job",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = ShramIcons.Close, contentDescription = if (isHindi) "बंद करें" else "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                ShramTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = null },
                    label = if (isHindi) "काम का नाम *" else "Job Title *",
                    placeholder = if (isHindi) "उदा. फ्लैट में टाइल फिटिंग" else "e.g. Tile Fitting in Flat"
                )

                Spacer(modifier = Modifier.height(10.dp))

                ShramTextField(
                    value = workerType,
                    onValueChange = { workerType = it },
                    label = if (isHindi) "कारीगर प्रकार *" else "Worker Category *",
                    placeholder = if (isHindi) "उदा. राजमिस्त्री" else "e.g. Mason"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShramTextField(
                        value = rateText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) rateText = it },
                        label = if (isHindi) "दैनिक मजदूरी (₹) *" else "Daily Wage (₹) *",
                        placeholder = "750",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    ShramTextField(
                        value = workersNeededText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) workersNeededText = it },
                        label = if (isHindi) "कामगार संख्या *" else "Workers Needed *",
                        placeholder = "2",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ShramTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = if (isHindi) "कार्य स्थल *" else "Site Location *",
                    placeholder = if (isHindi) "उदा. सेक्टर 150, ग्रेटर नोएडा" else "e.g. Sector 150, Greater Noida"
                )

                Spacer(modifier = Modifier.height(10.dp))

                ShramTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = if (isHindi) "विवरण" else "Requirements",
                    placeholder = if (isHindi) "उदा. 5 दिन का काम, शाम को दैनिक भुगतान" else "e.g. 5 days work, daily evening payout"
                )

                Spacer(modifier = Modifier.height(18.dp))

                ShramPrimaryButton(
                    text = if (isHindi) "काम पोस्ट करें" else "Publish Job",
                    onClick = {
                        if (title.isBlank()) {
                            errorMessage = if (isHindi) "कृपया काम का शीर्षक दर्ज करें" else "Please enter job title"
                            return@ShramPrimaryButton
                        }
                        val rate = rateText.toDoubleOrNull() ?: 500.0
                        val workers = workersNeededText.toIntOrNull() ?: 1
                        val newJob = Job(
                            jobId = java.util.UUID.randomUUID().toString(),
                            title = title.trim(),
                            workerType = workerType.trim(),
                            rate = rate,
                            rateType = RateType.DAILY,
                            workersRequired = workers,
                            location = location.trim(),
                            description = description.trim(),
                            status = JobStatus.OPEN,
                            date = "Today",
                            time = "Immediate",
                            createdAt = System.currentTimeMillis()
                        )
                        onPostJob(newJob)
                    }
                )
            }
        }
    }
}

/**
 * Dashboard Top Bar matching the approved reference mockups with Logo, Tagline, Location, and Notification Bell.
 */
@Composable
fun ShramDashboardTopBar(
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    title: String = "श्रम सारथी",
    subtitle: String = "मजदूरों का साथी, बेहतर कल की ओर",
    locationName: String = "Greater Noida",
    notificationCount: Int = 3,
    hasUnreadNotifications: Boolean = true
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Logo Badge + App Title & Tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                BrandLogoBadge(size = 38)

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Right: Location Pin & Notifications Bell with Red Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (locationName.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = ShramIcons.LocationPin,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "$locationName ⌵",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = ShramIcons.Notifications,
                            contentDescription = "Notifications",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        if (notificationCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(Color(0xFFDC2626), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$notificationCount",
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Persona / Role Switcher icon
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = ShramIcons.SwapHoriz,
                        contentDescription = "Switch Role",
                        tint = TealPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

/**
 * Friendly Assistant Card with a short Hindi greeting and 3 quick actions ("काम खोजें", "हाज़िरी", "मदद").
 */
@Composable
fun ShramAssistantCard(
    userName: String,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    greetingTitle: String = "नमस्ते",
    quickAction1: Pair<String, ImageVector> = Pair("काम खोजें", ShramIcons.Work),
    quickAction2: Pair<String, ImageVector> = Pair("हाज़िरी", ShramIcons.Calendar),
    quickAction3: Pair<String, ImageVector> = Pair("मदद", ShramIcons.Support)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(TealContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userName.isNotBlank()) userName.take(1).uppercase() else "स",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "$greetingTitle, ${userName.ifBlank { "साथी" }}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "आज आपकी क्या सहायता करूँ?",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(SaffronContainer, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "सारथी AI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Quick Action Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(quickAction1, quickAction2, quickAction3).forEach { (label, icon) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                            .clickable { onActionClick(label) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Large Central Microphone Button with caption "बोलकर पूछें".
 */
@Composable
fun ShramVoiceMicCenterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    caption: String = "बोलकर पूछें",
    subtitle: String = "माइक दबाएं और अपनी भाषा में कहें"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            TealPrimaryLight,
                            TealPrimary
                        )
                    )
                )
                .border(3.dp, SaffronAccent.copy(alpha = 0.5f), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ShramIcons.Mic,
                contentDescription = caption,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = caption,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = TextMuted
        )
    }
}

/**
 * Three compact service cards (Find Work, Attendance, Wages).
 */
@Composable
fun ShramServiceCardsRow(
    card1Title: String = "काम खोजें",
    card1Sub: String = "Find Work",
    card1Icon: ImageVector = ShramIcons.Work,
    onCard1Click: () -> Unit,
    card2Title: String = "हाज़िरी",
    card2Sub: String = "Attendance",
    card2Icon: ImageVector = ShramIcons.Calendar,
    onCard2Click: () -> Unit,
    card3Title: String = "वेतन",
    card3Sub: String = "Wages",
    card3Icon: ImageVector = ShramIcons.CurrencyRupee,
    onCard3Click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Card 1
        Card(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .clickable { onCard1Click() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(TealContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = card1Icon,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = card1Title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = card1Sub,
                    fontSize = 10.sp,
                    color = TextMuted,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Card 2
        Card(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .clickable { onCard2Click() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(SaffronContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = card2Icon,
                        contentDescription = null,
                        tint = SaffronAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = card2Title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = card2Sub,
                    fontSize = 10.sp,
                    color = TextMuted,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Card 3
        Card(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .clickable { onCard3Click() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFECFDF5), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = card3Icon,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = card3Title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = card3Sub,
                    fontSize = 10.sp,
                    color = TextMuted,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Concise nearby job card with clear rate, trade, location, and apply button.
 */
@Composable
fun ShramCompactJobCard(
    job: Job,
    hasApplied: Boolean,
    onApplyClick: () -> Unit,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = job.title.ifBlank { job.workerType },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(TealContainer, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = job.workerType,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ShramIcons.LocationPin,
                        contentDescription = null,
                        tint = SaffronAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = job.location.ifBlank { "Jaipur, Rajasthan" },
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI
                    Text(
                        text = "• ${job.workersRequired} " + (if (isHindi) "पद" else "openings"),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI
                Text(
                    text = "₹${job.rate.toInt()} / ${job.rateType.getUnitLabel(isHindi)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealPrimary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Button
            val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI
            if (hasApplied) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFECFDF5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isHindi) "✓ आवेदन किया" else "✓ Applied",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TealPrimary)
                        .clickable { onApplyClick() }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = if (isHindi) "आवेदन करें" else "Apply Now",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

