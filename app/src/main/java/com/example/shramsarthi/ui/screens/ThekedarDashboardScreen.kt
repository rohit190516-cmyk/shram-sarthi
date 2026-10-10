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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobStatus
import com.example.shramsarthi.model.User
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.ui.components.ShramAssistantCard
import com.example.shramsarthi.ui.components.ShramDashboardTopBar
import com.example.shramsarthi.ui.components.ShramEmergencySheet
import com.example.shramsarthi.ui.components.ShramEmptyState
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramOutlinedButton
import com.example.shramsarthi.ui.components.ShramPostJobDialog
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.components.ShramServiceCardsRow
import com.example.shramsarthi.ui.components.ShramVoiceMicCenterButton
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.RoleThekedarColor
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ThekedarDashboardScreen(
    onNavigateBack: () -> Unit,
    onLogoutClick: () -> Unit,
    onSwitchRoleClick: () -> Unit,
    onOpenTermsClick: () -> Unit = {}
) {
    val isHindi = LocalAppLanguage.current == AppLanguage.HINDI
    val role = UserRole.THEKEDAR
    val currentUid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: ""
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // User Profile state
    var currentUser by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(currentUid) {
        if (currentUid.isNotBlank()) {
            val userResult = ShramSarthiRepositoryProvider.userRepository.getUser(currentUid)
            currentUser = userResult.getOrNull()
        }
    }

    var showPostJobDialog by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }

    // Live Jobs posted by this Thekedar from Firestore
    val myPostedJobs by ShramSarthiRepositoryProvider.jobRepository
        .observeJobsByThekedar(currentUid)
        .collectAsState(initial = emptyList())

    // All open jobs fallback
    val allOpenJobs by ShramSarthiRepositoryProvider.jobRepository
        .observeOpenJobs()
        .collectAsState(initial = emptyList())

    val displayedJobs = if (myPostedJobs.isNotEmpty()) myPostedJobs else allOpenJobs

    // Calculate real metrics backed by actual data
    val activeJobsCount = displayedJobs.count { it.status == JobStatus.OPEN }
    val totalWorkersNeeded = displayedJobs.sumOf { it.workersRequired }

    Scaffold(
        topBar = {
            ShramDashboardTopBar(
                modifier = Modifier.statusBarsPadding(),
                onMenuClick = onSwitchRoleClick,
                onNotificationClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            if (isHindi) "कोई नई सूचना नहीं है" else "No new notifications"
                        )
                    }
                },
                title = if (isHindi) "श्रम सारथी" else "Shram Sarthi",
                subtitle = if (isHindi) "ठेकेदार पोर्टल" else "Contractor Portal"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Friendly Assistant Card for Thekedar
            item {
                Spacer(modifier = Modifier.height(6.dp))
                ShramAssistantCard(
                    userName = currentUser?.fullName ?: if (isHindi) "ठेकेदार जी" else "Contractor",
                    greetingTitle = if (isHindi) "नमस्ते" else "Welcome",
                    quickAction1 = Pair(if (isHindi) "नया काम" else "Post Job", ShramIcons.Add),
                    quickAction2 = Pair(if (isHindi) "हाज़िरी" else "Attendance", ShramIcons.Calendar),
                    quickAction3 = Pair(if (isHindi) "कारीगर" else "Workers", ShramIcons.Group),
                    onActionClick = { action ->
                        when {
                            action == "नया काम" || action == "Post Job" -> showPostJobDialog = true
                            action == "हाज़िरी" || action == "Attendance" -> coroutineScope.launch {
                                snackbarHostState.showSnackbar(if (isHindi) "हाज़िरी रजिस्टर सक्रिय है" else "Attendance register is active")
                            }
                            action == "कारीगर" || action == "Workers" -> coroutineScope.launch {
                                snackbarHostState.showSnackbar(if (isHindi) "कारीगर सूची जांची जा रही है" else "Checking worker directory")
                            }
                        }
                    }
                )
            }

            // Central Microphone Button
            item {
                ShramVoiceMicCenterButton(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (isHindi) "🎤 बोलिए... कौन से कारीगर चाहिए या क्या काम पोस्ट करना है?"
                                else "🎤 Speak... What workers are needed or what job to post?"
                            )
                        }
                    },
                    caption = if (isHindi) "बोलकर पूछें" else "Voice Search",
                    subtitle = if (isHindi) "माइक दबाएं और साइट की मांग बताएं" else "Tap mic to speak your requirement"
                )
            }

            // 3 Compact Service Cards
            item {
                ShramServiceCardsRow(
                    card1Title = if (isHindi) "नया काम" else "Post Job",
                    card1Sub = if (isHindi) "मांग दर्ज करें" else "Post requirement",
                    card1Icon = ShramIcons.Add,
                    onCard1Click = { showPostJobDialog = true },
                    card2Title = if (isHindi) "हाज़िरी" else "Attendance",
                    card2Sub = if (isHindi) "दैनिक हाज़िरी" else "Daily roll",
                    card2Icon = ShramIcons.Calendar,
                    onCard2Click = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (isHindi) "हाज़िरी रजिस्टर देखा जा रहा है" else "Opening attendance register"
                            )
                        }
                    },
                    card3Title = if (isHindi) "कारीगर" else "Workers",
                    card3Sub = if (isHindi) "कारीगर खोजें" else "Find workers",
                    card3Icon = ShramIcons.Group,
                    onCard3Click = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (isHindi) "कारीगर खोज सुविधा सक्रिय" else "Worker search activated"
                            )
                        }
                    }
                )
            }

            // Real Metrics Overview
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricBox(
                            title = if (isHindi) "सक्रिय कार्य" else "Active Jobs",
                            value = "$activeJobsCount",
                            subtitle = if (isHindi) "खुले प्रोजेक्ट" else "Open projects",
                            color = RoleThekedarColor,
                            modifier = Modifier.weight(1f)
                        )
                        MetricBox(
                            title = if (isHindi) "आवश्यक कारीगर" else "Workers Needed",
                            value = "$totalWorkersNeeded",
                            subtitle = if (isHindi) "कुल मांग" else "Total demand",
                            color = TealPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Posted Requirements Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "साइट आवश्यकताएं" else "Site Requirements",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (myPostedJobs.isNotEmpty()) {
                                if (isHindi) "आपकी पोस्टिंग" else "Your Postings"
                            } else {
                                if (isHindi) "पोर्टल पर उपलब्ध कार्य" else "Available Site Work"
                            },
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(SaffronContainer, RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${displayedJobs.size} " + (if (isHindi) "काम" else "Jobs"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            if (displayedJobs.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
                        ShramEmptyState(
                            title = if (isHindi) "कोई काम पोस्ट नहीं हुआ" else "No Jobs Posted",
                            subtitle = if (isHindi) {
                                "कुशल राजमिस्त्री, इलेक्ट्रीशियन या सहायक कारीगरों को बुलाने के लिए आवश्यकता दर्ज करें।"
                            } else {
                                "Post requirements to hire skilled masons, electricians, or helpers."
                            },
                            buttonText = if (isHindi) "+ काम पोस्ट करें" else "+ Post Job",
                            onButtonClick = { showPostJobDialog = true }
                        )
                    }
                }
            } else {
                items(displayedJobs, key = { it.jobId }) { job ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = job.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${com.example.shramsarthi.model.UserRole.getLocalizedSkill(job.workerType, isHindi)} • ${job.location}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (job.status == JobStatus.OPEN) Color(0xFFECFDF5) else Color(0xFFF1F5F9),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (job.status == JobStatus.OPEN) (if (isHindi) "खुला है" else "Open") else job.status.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (job.status == JobStatus.OPEN) Color(0xFF047857) else TextMuted
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = BorderSubtle, thickness = 0.8.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "₹${job.rate.toInt()} " + (if (isHindi) "/ दिन" else "/ day"),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TealPrimary
                                    )

                                    Text(
                                        text = "${job.workersRequired} " + (if (isHindi) "कारीगर चाहिए" else "workers needed"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Actions
            item {
                Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                    Spacer(modifier = Modifier.height(18.dp))
                    ShramOutlinedButton(
                        text = if (isHindi) "भूमिका बदलें" else "Switch Role",
                        onClick = onSwitchRoleClick,
                        borderColor = RoleThekedarColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ShramOutlinedButton(
                        text = if (isHindi) "नियम व शर्तें" else "Terms & Policy",
                        onClick = onOpenTermsClick,
                        borderColor = TealPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ShramPrimaryButton(
                        text = if (isHindi) "लॉग आउट करें" else "Log Out",
                        onClick = onLogoutClick,
                        containerColor = Color(0xFFDC2626)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Post Job Dialog
    if (showPostJobDialog) {
        ShramPostJobDialog(
            initialCategory = if (isHindi) "राजमिस्त्री" else "Mason",
            onDismiss = { showPostJobDialog = false },
            onPostJob = { newJob ->
                showPostJobDialog = false
                val jobWithThekedar = newJob.copy(thekedarId = currentUid.ifBlank { "contractor_1" })
                coroutineScope.launch {
                    val result = ShramSarthiRepositoryProvider.jobRepository.createJob(jobWithThekedar)
                    result.onSuccess {
                        snackbarHostState.showSnackbar(
                            if (isHindi) "✓ कार्य सफलतापूर्वक पोस्ट हुआ!" else "✓ Job posted successfully!"
                        )
                    }.onFailure { exc ->
                        snackbarHostState.showSnackbar(
                            if (isHindi) "विफल: ${exc.localizedMessage}" else "Failed: ${exc.localizedMessage}"
                        )
                    }
                }
            }
        )
    }

    // Emergency SOS Sheet
    if (showEmergencyDialog) {
        ShramEmergencySheet(onDismiss = { showEmergencyDialog = false })
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
        }
    }
}
