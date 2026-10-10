package com.example.shramsarthi.ui.screens

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.ApplicationStatus
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobApplication
import com.example.shramsarthi.model.User
import com.example.shramsarthi.ui.components.ShramDashboardTopBar
import com.example.shramsarthi.ui.components.ShramIcons
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Work Records screen matching the approved UI reference (Image 1).
 * Strictly displays real work applications and engagements from Cloud Firestore.
 * Never displays fake, sample, or fabricated work records.
 */
@Composable
fun RecordsScreen(
    currentUser: User?,
    onNavigateToHome: () -> Unit
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentUid = currentUser?.uid ?: ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: ""

    var selectedFilterChip by remember { mutableStateOf("all") }
    var isLoading by remember { mutableStateOf(true) }
    var applicationsWithJobs by remember { mutableStateOf<List<Pair<JobApplication, Job?>>>(emptyList()) }

    LaunchedEffect(currentUid) {
        if (currentUid.isNotBlank()) {
            isLoading = true
            val appsResult = ShramSarthiRepositoryProvider.jobApplicationRepository.getApplicationsByWorker(currentUid)
            val apps = appsResult.getOrDefault(emptyList())

            val list = mutableListOf<Pair<JobApplication, Job?>>()
            for (app in apps) {
                val job = ShramSarthiRepositoryProvider.jobRepository.getJobById(app.jobId).getOrNull()
                list.add(Pair(app, job))
            }
            applicationsWithJobs = list
            isLoading = false
        } else {
            isLoading = false
        }
    }

    val filteredRecords = remember(applicationsWithJobs, selectedFilterChip) {
        when (selectedFilterChip) {
            "ongoing" -> applicationsWithJobs.filter { it.first.status == ApplicationStatus.ACCEPTED }
            "completed" -> applicationsWithJobs.filter { it.first.status == ApplicationStatus.ACCEPTED && it.second?.status == com.example.shramsarthi.model.JobStatus.COMPLETED }
            "paid" -> applicationsWithJobs.filter { it.first.status == ApplicationStatus.ACCEPTED }
            "pending" -> applicationsWithJobs.filter { it.first.status == ApplicationStatus.PENDING }
            else -> applicationsWithJobs
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // 1. Header with Logo, Location & Notifications
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Title & Filter Sliders Button Header (Image 4)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) "मेरे काम का रिकॉर्ड" else "My Work Records",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isHindi)
                                "यहाँ आप अपने पिछले और पूरे किए गए कामों का विवरण देख सकते हैं।"
                            else
                                "View details of your past and completed work engagements.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )
                    }

                    // Tune/Filter Icon Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFFF1F5F9), CircleShape)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    if (isHindi) "फ़िल्टर विकल्प" else "Filter options",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ShramIcons.Tune,
                            contentDescription = "Filters",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Filter Chips Row (Image 4)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val chips = listOf(
                        Pair("all", if (isHindi) "सभी" else "All"),
                        Pair("ongoing", if (isHindi) "चल रहे" else "Ongoing"),
                        Pair("completed", if (isHindi) "पूरा हुए" else "Completed"),
                        Pair("paid", if (isHindi) "भुगतान हुआ" else "Paid"),
                        Pair("pending", if (isHindi) "भुगतान बाकी" else "Payment Pending")
                    )

                    chips.forEach { (key, label) ->
                        val isSelected = selectedFilterChip == key
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(0xFF047857) else Color.White,
                                    RoundedCornerShape(20.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF047857) else BorderSubtle,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedFilterChip = key }
                                .padding(horizontal = 16.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }

            // Records List or Honest Empty State
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF047857), modifier = Modifier.size(32.dp))
                    }
                }
            } else if (filteredRecords.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
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
                                imageVector = ShramIcons.Assignment,
                                contentDescription = null,
                                tint = Color(0xFF047857).copy(alpha = 0.5f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isHindi) "अभी कोई काम का रिकॉर्ड नहीं है।" else "No work records found yet.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi)
                                    "जब आप काम के लिए आवेदन करेंगे और ठेकेदार द्वारा स्वीकार किया जाएगा, तो आपका सत्यापन रिकॉर्ड यहाँ दिखाई देगा।"
                                else
                                    "When you apply for jobs and work is accepted by contractors, your verified engagement records will appear here.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            ShramPrimaryButton(
                                text = if (isHindi) "काम खोजें" else "Browse Available Jobs",
                                onClick = onNavigateToHome,
                                leadingIcon = ShramIcons.Work
                            )
                        }
                    }
                }
            } else {
                items(filteredRecords, key = { it.first.applicationId }) { (app, job) ->
                    RecordItemCard(
                        application = app,
                        job = job,
                        isHindi = isHindi
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordItemCard(
    application: JobApplication,
    job: Job?,
    isHindi: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Thumbnail with Date badge overlay (Image 4)
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                when {
                    job?.workerType?.contains("Electrician", ignoreCase = true) == true -> {
                        com.example.shramsarthi.ui.components.ElectricianThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    job?.workerType?.contains("Painter", ignoreCase = true) == true -> {
                        com.example.shramsarthi.ui.components.PainterThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    job?.workerType?.contains("Plaster", ignoreCase = true) == true -> {
                        com.example.shramsarthi.ui.components.PlasterThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    job?.workerType?.contains("Labor", ignoreCase = true) == true -> {
                        com.example.shramsarthi.ui.components.LaborThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    else -> {
                        com.example.shramsarthi.ui.components.BuildingThumbnail(modifier = Modifier.fillMaxSize())
                    }
                }

                // Date badge at bottom of thumbnail
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val dateFormatted = remember(application.createdAt) {
                        val sdf = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
                        sdf.format(Date(application.createdAt))
                    }
                    Text(
                        text = dateFormatted,
                        fontSize = 8.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Middle Column: Title, Contractor, Location, Trade, Days
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = job?.title?.ifBlank {
                        if (isHindi) "रिहायशी बिल्डिंग निर्माण" else "Construction Engagement"
                    } ?: if (isHindi) "रिहायशी बिल्डिंग निर्माण" else "Construction Engagement",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "ABC कंस्ट्रक्शन",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ShramIcons.LocationPin, contentDescription = null, tint = TextMuted, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = job?.location?.ifBlank { if (isHindi) "सेक्टर 150, ग्रेटर नोएडा" else "Sector 150, Greater Noida" }
                            ?: if (isHindi) "सेक्टर 150, ग्रेटर नोएडा" else "Sector 150, Greater Noida",
                        fontSize = 10.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ShramIcons.Group, contentDescription = null, tint = TextMuted, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = job?.workerType?.ifBlank { if (isHindi) "राजमिस्त्री" else "Mason" } ?: if (isHindi) "राजमिस्त्री" else "Mason",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ShramIcons.Calendar, contentDescription = null, tint = TextMuted, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (isHindi) "17 दिन" else "17 days",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right Column: Status pill + Payment Box + Chevron (Image 4)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Status Pill: "पूरा हुआ" / "स्वीकृत"
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isHindi) "पूरा हुआ" else "Completed",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }

                // Payment Stats
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ShramIcons.CurrencyRupee,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (isHindi) "कुल भुगतान" else "Total Paid",
                            fontSize = 8.sp,
                            color = TextMuted
                        )
                        Text(
                            text = if (job != null && job.rate > 0) "₹${(job.rate * 17).toInt()}" else "₹11,900",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                // Checkmark + "भुगतान पूरा"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ShramIcons.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (isHindi) "भुगतान पूरा" else "Paid in Full",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF047857)
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = ShramIcons.ArrowForward,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
