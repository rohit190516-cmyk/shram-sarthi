package com.example.shramsarthi.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.Complaint
import com.example.shramsarthi.model.ComplaintCategory
import com.example.shramsarthi.model.ComplaintStatus
import com.example.shramsarthi.model.User
import com.example.shramsarthi.ui.components.ShramDashboardTopBar
import com.example.shramsarthi.ui.components.ShramEmptyState
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramOutlinedButton
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryDark
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage
import com.example.shramsarthi.voice.VoiceSpeechManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Complaints section UI matching the approved Shram Sarthi mockup.
 * Displays genuine persisted complaints from Cloud Firestore,
 * AI Saathi voice integration, and a step-by-step guided complaint builder.
 */
@Composable
fun ComplaintsScreen(
    currentUser: User?,
    onNavigateBack: () -> Unit = {}
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentUid = currentUser?.uid ?: ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: ""

    // Real complaints from Firestore Flow
    val complaintsList by ShramSarthiRepositoryProvider.complaintRepository
        .observeComplaintsByWorker(currentUid)
        .collectAsState(initial = emptyList())

    // Guided Complaint Flow Dialog State
    var showGuidedFlow by remember { mutableStateOf(false) }
    var selectedCategoryForFlow by remember { mutableStateOf(ComplaintCategory.UNPAID_WAGES) }
    var selectedComplaintDetail by remember { mutableStateOf<Complaint?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Top Header with Logo, Location & Notification Bell
            item {
                ShramDashboardTopBar(
                    title = if (isHindi) "श्रम सारथी" else "Shram Sarthi",
                    subtitle = if (isHindi) "मजदूरों का साथी, बेहतर कल की ओर" else "Worker's Companion, Towards a Better Tomorrow",
                    locationName = currentUser?.location?.ifBlank {
                        if (isHindi) "ग्रेटर नोएडा" else "Greater Noida"
                    } ?: if (isHindi) "ग्रेटर नोएडा" else "Greater Noida",
                    notificationCount = 3,
                    onNotificationClick = {
                        Toast.makeText(
                            context,
                            if (isHindi) "कोई नई सूचना नहीं है" else "No new notifications",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // 2. Prominent AI Saathi Hero Card
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ComplaintsAiHeroCard(
                    isHindi = isHindi,
                    onVoiceComplaintClick = {
                        selectedCategoryForFlow = ComplaintCategory.UNPAID_WAGES
                        showGuidedFlow = true
                    }
                )
            }

            // 3. Category Selection ("शिकायत का प्रकार चुनें")
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isHindi) "शिकायत का प्रकार चुनें" else "Select Complaint Type",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )

                ComplaintCategoriesRow(
                    isHindi = isHindi,
                    onCategorySelected = { cat ->
                        selectedCategoryForFlow = cat
                        showGuidedFlow = true
                    }
                )
            }

            // 4. Complaint Process Stepper ("क्या है शिकायत प्रक्रिया?")
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ComplaintProcessTrack(isHindi = isHindi)
            }

            // 5. My Complaints Header ("मेरी शिकायतें")
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "मेरी शिकायतें" else "My Complaints",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    if (complaintsList.isNotEmpty()) {
                        Text(
                            text = if (isHindi) "कुल: ${complaintsList.size}" else "Total: ${complaintsList.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary
                        )
                    }
                }
            }

            // 6. List of Real Complaints or Genuine Empty State
            if (complaintsList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = ShramIcons.ShieldAlert,
                                contentDescription = null,
                                tint = TealPrimary.copy(alpha = 0.5f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isHindi) "अभी कोई शिकायत नहीं है।" else "You have no complaints yet.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi)
                                    "अपनी समस्या दर्ज करने के लिए ऊपर 'बोलकर शिकायत करें' दबाएं या किसी श्रेणी को चुनें।"
                                else
                                    "Tap 'Voice Complaint' above or select a category to prepare a verified complaint draft.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            ShramPrimaryButton(
                                text = if (isHindi) "नई शिकायत शुरू करें" else "Start a Complaint",
                                onClick = {
                                    selectedCategoryForFlow = ComplaintCategory.UNPAID_WAGES
                                    showGuidedFlow = true
                                },
                                leadingIcon = ShramIcons.Add
                            )
                        }
                    }
                }
            } else {
                items(complaintsList, key = { it.complaintId }) { complaint ->
                    ComplaintItemCard(
                        complaint = complaint,
                        isHindi = isHindi,
                        onClick = { selectedComplaintDetail = complaint }
                    )
                }
            }
        }
    }

    // Modal: Guided Complaint Preparation
    if (showGuidedFlow) {
        GuidedComplaintDialog(
            currentUser = currentUser,
            initialCategory = selectedCategoryForFlow,
            isHindi = isHindi,
            onDismiss = { showGuidedFlow = false },
            onComplaintSaved = { savedComplaint ->
                coroutineScope.launch {
                    ShramSarthiRepositoryProvider.complaintRepository.saveComplaint(savedComplaint)
                }
                showGuidedFlow = false
                Toast.makeText(
                    context,
                    if (isHindi) "शिकायत ड्राफ्ट सफलतापूर्वक सुरक्षित कर लिया गया है" else "Complaint draft saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    // Modal: Complaint Detail & Export/Share View
    selectedComplaintDetail?.let { complaint ->
        ComplaintDetailDialog(
            complaint = complaint,
            isHindi = isHindi,
            onDismiss = { selectedComplaintDetail = null },
            onDelete = {
                coroutineScope.launch {
                    ShramSarthiRepositoryProvider.complaintRepository.deleteComplaint(complaint.complaintId)
                }
                selectedComplaintDetail = null
            }
        )
    }
}

/**
 * Prominent AI Saathi Hero Card matching Image 1 with Robot Mascot,
 * Speech Bubble with sample prompts, and Voice Action pill button.
 */
@Composable
private fun ComplaintsAiHeroCard(
    isHindi: Boolean,
    onVoiceComplaintClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7).copy(alpha = 0.4f))
                    ),
                    RoundedCornerShape(20.dp)
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Robot Mascot + Title + Voice Button
                Column(modifier = Modifier.weight(1.1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        com.example.shramsarthi.ui.components.RobotSaathiIllustration(
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "AI साथी से बात करें" else "Talk to AI Saathi",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B5E20)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isHindi)
                                    "अपनी समस्या बताएं, कानून समझें\nऔर शिकायत तैयार करें"
                                else
                                    "Explain your problem, know laws\n& prepare complaint",
                                fontSize = 10.sp,
                                color = Color(0xFF2E7D32),
                                lineHeight = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Green Pill Voice Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF047857))
                            .clickable(onClick = onVoiceComplaintClick)
                            .padding(vertical = 9.dp, horizontal = 14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ShramIcons.Mic,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "बोलकर शिकायत करें ›" else "Voice Complaint ›",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: Speech Bubble with 3 sample prompts (Image 1)
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .background(Color.White, RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (isHindi) "\"मेरी मजदूरी नहीं मिली\"" else "\"My wages not paid\"",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isHindi) "\"ठेकेदार ने पैसा देने से मना किया\"" else "\"Contractor refused to pay\"",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isHindi) "\"काम पर सुरक्षा की समस्या है\"" else "\"Worksite safety hazard\"",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

/**
 * 6 Category Cards matching Image 1.
 */
@Composable
private fun ComplaintCategoriesRow(
    isHindi: Boolean,
    onCategorySelected: (ComplaintCategory) -> Unit
) {
    val categories = listOf(
        CategoryItemData(ComplaintCategory.UNPAID_WAGES, if (isHindi) "मजदूरी\nनहीं मिली" else "Unpaid\nWages", Color(0xFFE8F5E9), Color(0xFF047857), ShramIcons.CurrencyRupee),
        CategoryItemData(ComplaintCategory.UNFAIR_TREATMENT, if (isHindi) "अनुचित\nव्यवहार" else "Unfair\nBehavior", Color(0xFFFFEBEE), Color(0xFFC62828), ShramIcons.Group),
        CategoryItemData(ComplaintCategory.UNSAFE_WORKPLACE, if (isHindi) "असुरक्षित\nकार्यस्थल" else "Unsafe\nWorkplace", Color(0xFFE0F2F1), Color(0xFF00695C), ShramIcons.Shield),
        CategoryItemData(ComplaintCategory.WORK_DISPUTE, if (isHindi) "काम का\nविवाद" else "Work\nDispute", Color(0xFFEDE7F6), Color(0xFF4527A0), ShramIcons.Description),
        CategoryItemData(ComplaintCategory.DELAYED_PAYMENT, if (isHindi) "देरी से\nभुगतान" else "Delayed\nPayment", Color(0xFFFFF3E0), Color(0xFFE65100), ShramIcons.Schedule),
        CategoryItemData(ComplaintCategory.OTHER, if (isHindi) "अन्य\nसमस्या" else "Other\nProblem", Color(0xFFE3F2FD), Color(0xFF1565C0), ShramIcons.MoreHoriz)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEachIndexed { index, item ->
            val isFirstSelected = index == 0
            Card(
                modifier = Modifier
                    .width(88.dp)
                    .height(98.dp)
                    .clickable { onCategorySelected(item.category) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFirstSelected) Color(0xFFE8F5E9) else Color.White
                ),
                border = BorderStroke(
                    width = if (isFirstSelected) 1.5.dp else 1.dp,
                    color = if (isFirstSelected) Color(0xFF047857) else BorderSubtle
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(item.bgColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = item.iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

private data class CategoryItemData(
    val category: ComplaintCategory,
    val title: String,
    val bgColor: Color,
    val iconColor: Color,
    val icon: ImageVector
)

/**
 * 5-Step Process Stepper matching Image 1 ("क्या है शिकायत प्रक्रिया?")
 */
@Composable
private fun ComplaintProcessTrack(isHindi: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (isHindi) "क्या है शिकायत प्रक्रिया?" else "What is the Complaint Process?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))

            val steps = listOf(
                ProcessStepData(1, if (isHindi) "समस्या बताएं" else "State Issue", if (isHindi) "अपनी बात लिखें या बोलें" else "Write or speak", ShramIcons.Edit),
                ProcessStepData(2, if (isHindi) "जानकारी दें" else "Give Info", if (isHindi) "तारीख, जगह, ठेकेदार, राशि आदि" else "Dates, place, amount", ShramIcons.Description),
                ProcessStepData(3, if (isHindi) "सबूत जोड़ें" else "Add Evidence", if (isHindi) "फोटो, रसीद, मेसेज, रिकॉर्ड" else "Photos, slips, records", ShramIcons.Photo),
                ProcessStepData(4, if (isHindi) "AI तैयारी करेगा" else "AI Prepares", if (isHindi) "कानून बताएगा और शिकायत तैयार करेगा" else "Cites laws & drafts", ShramIcons.SmartToy),
                ProcessStepData(5, if (isHindi) "आप समीक्षा करें\nऔर जमा करें" else "Review & Submit", if (isHindi) "सही करके सेव या शेयर करें" else "Save or share draft", ShramIcons.Send)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                steps.forEach { step ->
                    Column(
                        modifier = Modifier.width(115.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Badge Number Circle
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFF047857), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${step.stepNumber}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Icon in Pale Green Circle
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFE8F5E9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = step.icon,
                                contentDescription = null,
                                tint = Color(0xFF047857),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = step.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 13.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = step.desc,
                            fontSize = 9.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 11.sp
                        )
                    }
                }
            }
        }
    }
}

private data class ProcessStepData(
    val stepNumber: Int,
    val title: String,
    val desc: String,
    val icon: ImageVector
)

/**
 * Individual saved complaint card matching Image 1 layout.
 */
@Composable
private fun ComplaintItemCard(
    complaint: Complaint,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Colored Icon Circle + Title, Subtitle & Calendar Date
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            when (complaint.category) {
                                ComplaintCategory.UNPAID_WAGES -> Color(0xFFFFEBEE)
                                ComplaintCategory.UNSAFE_WORKPLACE -> Color(0xFFFFEBEE)
                                ComplaintCategory.WORK_DISPUTE -> Color(0xFFEDE7F6)
                                ComplaintCategory.UNFAIR_TREATMENT -> Color(0xFFFFEBEE)
                                ComplaintCategory.DELAYED_PAYMENT -> Color(0xFFFFF3E0)
                                ComplaintCategory.OTHER -> Color(0xFFE3F2FD)
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (complaint.category) {
                            ComplaintCategory.UNPAID_WAGES -> ShramIcons.CurrencyRupee
                            ComplaintCategory.UNSAFE_WORKPLACE -> ShramIcons.ShieldAlert
                            ComplaintCategory.WORK_DISPUTE -> ShramIcons.Description
                            ComplaintCategory.UNFAIR_TREATMENT -> ShramIcons.Group
                            ComplaintCategory.DELAYED_PAYMENT -> ShramIcons.Schedule
                            ComplaintCategory.OTHER -> ShramIcons.MoreHoriz
                        },
                        contentDescription = null,
                        tint = when (complaint.category) {
                            ComplaintCategory.UNPAID_WAGES -> Color(0xFFDC2626)
                            ComplaintCategory.UNSAFE_WORKPLACE -> Color(0xFFDC2626)
                            ComplaintCategory.WORK_DISPUTE -> Color(0xFF4527A0)
                            ComplaintCategory.UNFAIR_TREATMENT -> Color(0xFFDC2626)
                            ComplaintCategory.DELAYED_PAYMENT -> Color(0xFFE65100)
                            ComplaintCategory.OTHER -> Color(0xFF1565C0)
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (isHindi) complaint.category.hindiTitle else complaint.category.englishTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = complaint.employerName.ifBlank {
                            if (isHindi) "ABC कंस्ट्रक्शन" else "ABC Construction"
                        },
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ShramIcons.Calendar,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        val formattedDate = remember(complaint.createdAt) {
                            val sdf = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
                            sdf.format(Date(complaint.createdAt))
                        }
                        Text(
                            text = formattedDate,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: Status Pill + Subtitle + Chevron (Image 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    // Status Pill
                    Box(
                        modifier = Modifier
                            .background(
                                when (complaint.status) {
                                    ComplaintStatus.DRAFT -> Color(0xFFFEF3C7) // Amber/Yellow
                                    ComplaintStatus.IN_PROGRESS -> Color(0xFFE0F2FE) // Blue
                                    ComplaintStatus.SUBMITTED -> Color(0xFFDCFCE7) // Green
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = when (complaint.status) {
                                ComplaintStatus.DRAFT -> if (isHindi) "तैयार हुई" else "Draft Ready"
                                ComplaintStatus.IN_PROGRESS -> if (isHindi) "प्रगति में" else "In Progress"
                                ComplaintStatus.SUBMITTED -> if (isHindi) "जमा किया गया" else "Submitted"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (complaint.status) {
                                ComplaintStatus.DRAFT -> Color(0xFFB45309)
                                ComplaintStatus.IN_PROGRESS -> Color(0xFF0284C7)
                                ComplaintStatus.SUBMITTED -> Color(0xFF15803D)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = when (complaint.status) {
                            ComplaintStatus.DRAFT -> if (isHindi) "शिकायत ड्राफ्ट तैयार है" else "Draft prepared"
                            ComplaintStatus.IN_PROGRESS -> if (isHindi) "जानकारी एकत्र की जा रही है" else "Gathering info"
                            ComplaintStatus.SUBMITTED -> if (isHindi) "श्रम विभाग में भेजा गया" else "Sent to labor dept"
                        },
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = ShramIcons.ArrowForward,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * 5-Step Guided Complaint Dialog using voice input, AI analysis and formal drafting.
 */
@Composable
fun GuidedComplaintDialog(
    currentUser: User?,
    initialCategory: ComplaintCategory,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onComplaintSaved: (Complaint) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var currentStep by remember { mutableIntStateOf(1) } // 1..5

    // Form data state
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var problemDescription by remember { mutableStateOf("") }
    var employerName by remember { mutableStateOf("") }
    var workLocation by remember { mutableStateOf(currentUser?.location ?: "") }
    var workDates by remember { mutableStateOf("") }
    var wageAgreed by remember { mutableStateOf("") }
    var amountPaid by remember { mutableStateOf("") }
    var amountClaimed by remember { mutableStateOf("") }
    var requestedResolution by remember { mutableStateOf(if (isHindi) "बकाया मजदूरी का तत्काल भुगतान" else "Immediate payment of pending wages") }
    var evidenceList = remember { mutableStateListOf<String>() }
    var generatedDraft by remember { mutableStateOf("") }

    // Voice manager state
    val voiceManager = remember {
        VoiceSpeechManager(context) { text ->
            if (currentStep == 1) {
                problemDescription = if (problemDescription.isBlank()) text else "$problemDescription $text"
            }
        }
    }
    val isListening by voiceManager.isListening.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            voiceManager.release()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BackgroundLight),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with step indicator & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "शिकायत तैयारी (चरण $currentStep/5)" else "Complaint Builder (Step $currentStep of 5)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                        Text(
                            text = when (currentStep) {
                                1 -> if (isHindi) "1. अपनी समस्या बताएं" else "1. State the Issue"
                                2 -> if (isHindi) "2. आवश्यक विवरण दर्ज करें" else "2. Essential Details"
                                3 -> if (isHindi) "3. साक्ष्य व काम के रिकॉर्ड" else "3. Evidence & Records"
                                4 -> if (isHindi) "4. AI कानूनी विश्लेषण" else "4. AI Legal Analysis"
                                else -> if (isHindi) "5. ड्राफ्ट समीक्षा व सुरक्षित करें" else "5. Review & Finalize"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = ShramIcons.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderSubtle)

                Box(modifier = Modifier.weight(1f, fill = false)) {
                    when (currentStep) {
                        1 -> {
                            // Step 1: Issue Statement (Voice + Text Fallback)
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Text(
                                    text = if (isHindi)
                                        "बोलकर या लिखकर बताएं कि आपके साथ क्या हुआ:"
                                    else
                                        "Speak or type what happened:",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                // Large Mic Voice Box
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isListening) Color(0xFFFEF3C7) else TealContainer, RoundedCornerShape(16.dp))
                                        .border(1.dp, if (isListening) SaffronAccent else TealPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                        .clickable {
                                            if (isListening) {
                                                voiceManager.stopListening()
                                            } else {
                                                voiceManager.startListening(language = if (isHindi) "hi-IN" else "en-IN")
                                            }
                                        }
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = ShramIcons.Mic,
                                            contentDescription = null,
                                            tint = if (isListening) SaffronAccent else TealPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isListening)
                                                (if (isHindi) "सुन रहा हूँ... बोलें (माइक बंद करने के लिए टैप करें)" else "Listening... tap to stop")
                                            else
                                                (if (isHindi) "माइक दबाकर बोलें (Speech-to-Text)" else "Tap to speak (Speech-to-Text)"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isListening) Color(0xFFB45309) else TealPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = problemDescription,
                                    onValueChange = { problemDescription = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    label = { Text(if (isHindi) "समस्या का विवरण (यहीं टाइप या एडिट करें)" else "Issue description (editable)") },
                                    placeholder = {
                                        Text(if (isHindi) "उदाहरण: ठेकेदार ने 15 दिन काम कराकर ₹9,000 देने से मना कर दिया।" else "e.g., Contractor owes ₹9,000 for 15 days of masonry work.")
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        2 -> {
                            // Step 2: Essential Details (Party, Place, Wages)
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Text(
                                    text = if (isHindi)
                                        "वैकल्पिक या ज्ञात विवरण भरें (जो नहीं पता उसे खाली छोड़ सकते हैं):"
                                    else
                                        "Provide known details (you can leave unknown fields blank):",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = employerName,
                                    onValueChange = { employerName = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(if (isHindi) "ठेकेदार या नियोक्ता का नाम" else "Employer Name") },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = workLocation,
                                    onValueChange = { workLocation = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(if (isHindi) "कार्यस्थल का स्थान" else "Worksite Location") },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = workDates,
                                    onValueChange = { workDates = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(if (isHindi) "काम की तारीखें या अवधि" else "Work Duration") },
                                    placeholder = { Text(if (isHindi) "उदा. 10 जून 2026 से 25 जून 2026" else "e.g. 10 June to 25 June 2026") },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = wageAgreed,
                                        onValueChange = { wageAgreed = it },
                                        modifier = Modifier.weight(1f),
                                        label = { Text(if (isHindi) "तय दिहाड़ी (₹)" else "Agreed Wage (₹)") },
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    OutlinedTextField(
                                        value = amountClaimed,
                                        onValueChange = { amountClaimed = it },
                                        modifier = Modifier.weight(1f),
                                        label = { Text(if (isHindi) "बकाया राशि (₹)" else "Unpaid Amount (₹)") },
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                }
                            }
                        }

                        3 -> {
                            // Step 3: Evidence & Records
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Text(
                                    text = if (isHindi)
                                        "अपने पास उपलब्ध साक्ष्य या रिकॉर्ड चुनें (सभी को 'श्रमिक द्वारा दिया गया' चिह्नित किया जाएगा):"
                                    else
                                        "Select available evidence (all items are labelled as worker-provided):",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                val sampleEvidenceOptions = listOf(
                                    if (isHindi) "हाज़िरी डायरी या व्यक्तिगत नोट" else "Attendance Diary or Personal Note",
                                    if (isHindi) "WhatsApp संदेश या कॉल रिकॉर्ड" else "WhatsApp Message or Call Record",
                                    if (isHindi) "कार्यस्थल की तस्वीर" else "Worksite Photograph",
                                    if (isHindi) "साथी मजदूरों की गवाही" else "Co-worker Statement",
                                    if (isHindi) "ठेकेदार द्वारा दी गई पर्ची या रसीद" else "Contractor Slip or Receipt"
                                )

                                sampleEvidenceOptions.forEach { ev ->
                                    val isChecked = evidenceList.contains(ev)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(
                                                if (isChecked) TealContainer else Color.White,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                            .clickable {
                                                if (isChecked) evidenceList.remove(ev) else evidenceList.add(ev)
                                            }
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isChecked) ShramIcons.CheckCircle else ShramIcons.Add,
                                            contentDescription = null,
                                            tint = if (isChecked) TealPrimary else TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = ev,
                                            fontSize = 13.sp,
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                            color = TextPrimary
                                        )
                                    }
                                }

                                if (evidenceList.isEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isHindi)
                                            "* नोट: यदि कोई लिखित सबूत नहीं है, तो भी आप शिकायत दर्ज कर सकते हैं।"
                                        else
                                            "* Note: You can still file a complaint even if you do not have written slips.",
                                        fontSize = 11.sp,
                                        color = SaffronAccent
                                    )
                                }
                            }
                        }

                        4 -> {
                            // Step 4: AI Legal Analysis & Drafting
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                                    border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = ShramIcons.Scale,
                                                contentDescription = null,
                                                tint = Color(0xFF047857),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (isHindi) "सत्यापित कानूनी आधार (Statutory Reference)" else "Statutory Reference",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF047857)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (isHindi)
                                                "मजदूरी संहिता, 2019 (Code on Wages, 2019) की धारा 17 के तहत मजदूरी का समय पर भुगतान अनिवार्य है। गैर-भुगतान की स्थिति में श्रम आयुक्त या समाधान पोर्टल पर वाद दायर किया जा सकता है।"
                                            else
                                                "Under Section 17 of the Code on Wages, 2019, timely payment of wages is statutory. Claims can be filed via the Labour Commissioner or the SAMADHAN portal.",
                                            fontSize = 11.sp,
                                            color = TextPrimary,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (isHindi) "AI द्वारा तैयार औपचारिक शिकायत प्रारूप:" else "Generated Formal Complaint Draft:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                // Generate draft if empty
                                LaunchedEffect(Unit) {
                                    if (generatedDraft.isBlank()) {
                                        generatedDraft = buildFormalDraft(
                                            userName = currentUser?.fullName ?: (if (isHindi) "कारीगर" else "Worker"),
                                            userPhone = currentUser?.mobileNumber ?: "",
                                            userTrade = currentUser?.primarySkill ?: (if (isHindi) "निर्माण श्रमिक" else "Construction Worker"),
                                            category = selectedCategory,
                                            problemDescription = problemDescription,
                                            employerName = employerName,
                                            workLocation = workLocation,
                                            workDates = workDates,
                                            wageAgreed = wageAgreed,
                                            amountClaimed = amountClaimed,
                                            evidenceList = evidenceList,
                                            isHindi = isHindi
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = generatedDraft,
                                    onValueChange = { generatedDraft = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, lineHeight = 16.sp)
                                )
                            }
                        }

                        5 -> {
                            // Step 5: Final Review, Actions & Save
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, BorderSubtle)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = if (isHindi) "अंतिम शिकायत समीक्षा" else "Final Complaint Review",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TealPrimaryDark
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = generatedDraft,
                                            fontSize = 12.sp,
                                            color = TextPrimary,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Official Portal Action
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://samadhan.labour.gov.in"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (isHindi) "श्रम विभाग समाधान पोर्टल खोलें (samadhan.labour.gov.in) ↗" else "Open MoLE SAMADHAN Portal ↗",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TealPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Copy to clipboard action
                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(generatedDraft))
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "शिकायत प्रारूप कॉपी हो गया" else "Draft copied to clipboard",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (isHindi) "प्रारूप कॉपी करें (Copy Draft)" else "Copy Draft to Clipboard",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Step Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep -= 1 },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isHindi) "पीछे" else "Back")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (currentStep < 5) {
                        ShramPrimaryButton(
                            text = if (isHindi) "आगे बढ़ें ›" else "Next Step ›",
                            onClick = {
                                if (currentStep == 1 && problemDescription.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        if (isHindi) "कृपया समस्या का विवरण दें" else "Please describe the issue",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    currentStep += 1
                                }
                            }
                        )
                    } else {
                        ShramPrimaryButton(
                            text = if (isHindi) "ड्राफ्ट सुरक्षित करें" else "Save Complaint Draft",
                            onClick = {
                                val complaint = Complaint(
                                    ownerUid = currentUser?.uid ?: ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: "",
                                    category = selectedCategory,
                                    originalDescription = problemDescription,
                                    language = if (isHindi) "hi" else "en",
                                    employerName = employerName,
                                    workLocation = workLocation,
                                    workDates = workDates,
                                    wageAgreed = wageAgreed,
                                    amountPaid = amountPaid,
                                    amountClaimed = amountClaimed,
                                    requestedResolution = requestedResolution,
                                    draftContent = generatedDraft,
                                    evidenceReferences = evidenceList.toList(),
                                    status = ComplaintStatus.DRAFT
                                )
                                onComplaintSaved(complaint)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modal to view saved complaint details, copy, share or open official grievance portal.
 */
@Composable
fun ComplaintDetailDialog(
    complaint: Complaint,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isHindi) complaint.category.hindiTitle else complaint.category.englishTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isHindi) "स्थिति (Status):" else "Status:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Text(
                        text = if (isHindi) complaint.status.hindiLabel else complaint.status.englishLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (complaint.employerName.isNotBlank()) {
                    Text(
                        text = if (isHindi) "नियोक्ता: ${complaint.employerName}" else "Employer: ${complaint.employerName}",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
                if (complaint.workLocation.isNotBlank()) {
                    Text(
                        text = if (isHindi) "स्थान: ${complaint.workLocation}" else "Location: ${complaint.workLocation}",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
                if (complaint.amountClaimed.isNotBlank()) {
                    Text(
                        text = if (isHindi) "दावा की गई बकाया राशि: ₹${complaint.amountClaimed}" else "Claimed Unpaid Amount: ₹${complaint.amountClaimed}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronAccent
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isHindi) "शिकायत प्रारूप:" else "Complaint Draft:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = complaint.draftContent.ifBlank { complaint.originalDescription },
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Actions: Share & Copy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(complaint.draftContent))
                            Toast.makeText(
                                context,
                                if (isHindi) "कॉपी हो गया" else "Copied",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isHindi) "कॉपी करें" else "Copy", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, complaint.draftContent)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, if (isHindi) "शिकायत शेयर करें" else "Share Complaint")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isHindi) "शेयर करें" else "Share", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // SAMADHAN Portal Link
                OutlinedButton(
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(complaint.officialAuthorityUrl))
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isHindi) "समाधान पोर्टल पर शिकायत दर्ज करें ↗" else "File on SAMADHAN Portal ↗",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "बंद करें" else "Close")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626))
            ) {
                Text(if (isHindi) "हटाएं" else "Delete Draft")
            }
        }
    )
}

/**
 * Builds structured formal draft preserving user's words and statutory framework.
 */
private fun buildFormalDraft(
    userName: String,
    userPhone: String,
    userTrade: String,
    category: ComplaintCategory,
    problemDescription: String,
    employerName: String,
    workLocation: String,
    workDates: String,
    wageAgreed: String,
    amountClaimed: String,
    evidenceList: List<String>,
    isHindi: Boolean
): String {
    return if (isHindi) {
        """
सेवा में,
श्रम आयुक्त महोदय / सहायक श्रम आयुक्त कार्यालय
श्रम एवं रोजगार विभाग (समाधान पोर्टल)

विषय: मजदूरी संहिता, 2019 के अंतर्गत ${category.hindiTitle} एवं बकाया भुगतान के संबंध में शिकायत

महोदय,
सविनय निवेदन है कि मैं $userName, पेशा: $userTrade (मोबाइल: ${userPhone.ifBlank { "सत्यापित फोन" }}) निवासी ${workLocation.ifBlank { "ग्रेटर नोएडा" }} हूँ।

1. कार्य का विवरण: मैंने नियोक्ता/ठेकेदार ${employerName.ifBlank { "[नियोक्ता का नाम]" }} के अधीन ${workLocation.ifBlank { "[स्थान]" }} पर ${workDates.ifBlank { "[कार्य अवधि]" }} में कार्य किया।
2. तय दर एवं बकाया: मेरी तय दैनिक दिहाड़ी ₹${wageAgreed.ifBlank { "[तय राशि]" }} थी। कुल कार्य के पश्चात मेरा ₹${amountClaimed.ifBlank { "[बकाया राशि]" }} का भुगतान बकाया है।
3. घटना/समस्या: $problemDescription
4. संलग्न साक्ष्य (श्रमिक द्वारा प्रदान): ${if (evidenceList.isEmpty()) "मौखिक कार्य अनुबंध एवं साथी गवाह" else evidenceList.joinToString(", ")}।

प्रार्थना:
अतः श्रीमान से विनम्र निवेदन है कि मामले का संज्ञान लेते हुए नियोक्ता को बकाया राशि का वैधानिक ब्याज सहित तत्काल भुगतान करने हेतु निर्देशित करने की कृपा करें।

प्रार्थी:
$userName ($userTrade)
दिनांक: ${SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Date())}
(नोट: यह शिकायत प्रारूप प्रशासनिक शिकायत व समाधान पोर्टल के लिए तैयार किया गया है।)
        """.trimIndent()
    } else {
        """
To,
The District Labour Commissioner / Conciliation Officer
Department of Labour & Employment (SAMADHAN Grievance Cell)

Subject: Formal Grievance regarding ${category.englishTitle} under Section 17 of Code on Wages, 2019

Respected Authority,
I, $userName, working as $userTrade (Phone: ${userPhone.ifBlank { "Registered Phone" }}), resident of ${workLocation.ifBlank { "Greater Noida" }}, submit this formal representation:

1. Work Engagement: I was engaged by Contractor/Employer ${employerName.ifBlank { "[Employer Name]" }} at ${workLocation.ifBlank { "[Worksite]" }} during ${workDates.ifBlank { "[Work Period]" }}.
2. Agreed Wages & Arrears: The mutually agreed wage was ₹${wageAgreed.ifBlank { "[Agreed Rate]" }}. An outstanding amount of ₹${amountClaimed.ifBlank { "[Claimed Amount]" }} remains unpaid despite completion of work.
3. Statement of Issue: $problemDescription
4. Evidence Annexures (Worker-provided): ${if (evidenceList.isEmpty()) "Oral work agreement and co-worker testimonies" else evidenceList.joinToString(", ")}.

Relief Requested:
Kindly direct the concerned employer to release the outstanding dues of ₹${amountClaimed.ifBlank { "[Claimed Amount]" }} expeditiously along with statutory interest.

Complainant:
$userName ($userTrade)
Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Date())}
(Note: Generated administrative draft for submission to appropriate conciliation authority.)
        """.trimIndent()
    }
}
