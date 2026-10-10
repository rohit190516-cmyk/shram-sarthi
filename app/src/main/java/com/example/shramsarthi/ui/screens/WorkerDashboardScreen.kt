package com.example.shramsarthi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.R
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.ApplicationStatus
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobApplication
import com.example.shramsarthi.model.User
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.ui.components.ShramBottomNav
import com.example.shramsarthi.ui.components.ShramCompactJobCard
import com.example.shramsarthi.ui.components.ShramDashboardTopBar
import com.example.shramsarthi.ui.components.ShramEmptyState
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramJobDetailDialog
import com.example.shramsarthi.ui.components.ShramOutlinedButton
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.RoleWorkerColor
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryDark
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LanguageManager
import com.example.shramsarthi.util.LocalAppLanguage
import kotlinx.coroutines.launch

/**
 * Worker Dashboard managing the 4 approved tabs:
 * 1. Home (होम) - Nearby jobs, quick service tiles, voice search, active work
 * 2. Records (रिकॉर्ड) - Real Firestore work history and applications
 * 3. Complaints (शिकायत) - AI Saathi grievance builder & Firestore complaints
 * 4. Profile (प्रोफ़ाइल) - Profile details, stats, language switcher & settings
 */
@Composable
fun WorkerDashboardScreen(
    onNavigateBack: () -> Unit,
    onLogoutClick: () -> Unit,
    onSwitchRoleClick: () -> Unit,
    onOpenTermsClick: () -> Unit = {},
    onOpenAiSaathiClick: () -> Unit = {}
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: ""

    // User Profile state
    var currentUser by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(currentUid) {
        if (currentUid.isNotBlank()) {
            val userResult = ShramSarthiRepositoryProvider.userRepository.getUser(currentUid)
            currentUser = userResult.getOrNull()
        }
    }

    // Live Jobs from Firestore Flow
    val openJobsList by ShramSarthiRepositoryProvider.jobRepository
        .observeOpenJobs()
        .collectAsState(initial = emptyList())

    // Worker's Applications
    var myApplications by remember { mutableStateOf<List<JobApplication>>(emptyList()) }
    fun loadApplications() {
        if (currentUid.isNotBlank()) {
            coroutineScope.launch {
                val res = ShramSarthiRepositoryProvider.jobApplicationRepository.getApplicationsByWorker(currentUid)
                myApplications = res.getOrDefault(emptyList())
            }
        }
    }
    LaunchedEffect(currentUid) {
        loadApplications()
    }

    // Bottom Tab state: "home", "records", "complaints", "profile"
    var selectedTab by remember { mutableStateOf("home") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSkillFilter by remember { mutableStateOf<String?>(null) }
    var selectedJobForDetails by remember { mutableStateOf<Job?>(null) }
    var isApplying by remember { mutableStateOf(false) }
    val appliedJobIds = remember { mutableStateListOf<String>() }

    LaunchedEffect(myApplications) {
        appliedJobIds.clear()
        appliedJobIds.addAll(myApplications.map { it.jobId })
    }

    val filteredJobs = openJobsList.filter { job ->
        val matchesQuery = searchQuery.isBlank() ||
                job.title.contains(searchQuery, ignoreCase = true) ||
                job.workerType.contains(searchQuery, ignoreCase = true) ||
                job.location.contains(searchQuery, ignoreCase = true)

        val matchesSkill = selectedSkillFilter == null ||
                job.workerType.contains(selectedSkillFilter!!.split("/").first().trim(), ignoreCase = true) ||
                selectedSkillFilter!!.contains(job.workerType, ignoreCase = true)

        matchesQuery && matchesSkill
    }

    fun handleApplyForJob(job: Job) {
        if (appliedJobIds.contains(job.jobId)) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    if (isHindi) "आप इस काम के लिए पहले ही आवेदन कर चुके हैं।" else "You have already applied for this job."
                )
            }
            return
        }

        isApplying = true
        coroutineScope.launch {
            val application = JobApplication(
                applicationId = java.util.UUID.randomUUID().toString(),
                jobId = job.jobId,
                workerId = currentUid.ifBlank { "anonymous_worker" },
                thekedarId = job.thekedarId,
                status = ApplicationStatus.PENDING,
                createdAt = System.currentTimeMillis()
            )
            val result = ShramSarthiRepositoryProvider.jobApplicationRepository.applyForJob(application)
            isApplying = false
            result.onSuccess {
                appliedJobIds.add(job.jobId)
                loadApplications()
                snackbarHostState.showSnackbar(
                    if (isHindi) "✓ आवेदन सफलतापूर्वक जमा हुआ!" else "✓ Application submitted successfully!"
                )
            }.onFailure { exc ->
                snackbarHostState.showSnackbar(
                    if (isHindi) "त्रुटि: ${exc.localizedMessage ?: "सहेजा गया"}" else "Error: ${exc.localizedMessage ?: "Saved"}"
                )
                appliedJobIds.add(job.jobId)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            ShramBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                "home" -> {
                    // TAB 1: HOME (Image 3)
                    WorkerHomeContent(
                        currentUser = currentUser,
                        isHindi = isHindi,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onVoiceSearchClick = onOpenAiSaathiClick,
                        onFindWorkClick = {
                            searchQuery = ""
                            selectedSkillFilter = null
                        },
                        filteredJobs = filteredJobs,
                        appliedJobIds = appliedJobIds,
                        onApplyClick = { handleApplyForJob(it) },
                        onJobDetailsClick = { selectedJobForDetails = it },
                        myApplications = myApplications,
                        onViewAllJobsClick = {
                            searchQuery = ""
                            selectedSkillFilter = null
                        },
                        onViewAllActiveWorkClick = { selectedTab = "records" },
                        onOpenSchemesClick = onOpenAiSaathiClick
                    )
                }

                "records" -> {
                    // TAB 2: RECORDS (Image 1)
                    RecordsScreen(
                        currentUser = currentUser,
                        onNavigateToHome = { selectedTab = "home" }
                    )
                }

                "complaints" -> {
                    // TAB 3: COMPLAINTS (Image 4)
                    ComplaintsScreen(
                        currentUser = currentUser,
                        onNavigateBack = { selectedTab = "home" }
                    )
                }

                "profile" -> {
                    // TAB 4: PROFILE & SETTINGS (Image 2)
                    WorkerProfileContent(
                        currentUser = currentUser,
                        isHindi = isHindi,
                        onSwitchRoleClick = onSwitchRoleClick,
                        onOpenTermsClick = onOpenTermsClick,
                        onLogoutClick = onLogoutClick
                    )
                }
            }
        }
    }

    // Job Detail Modal Sheet
    selectedJobForDetails?.let { job ->
        ShramJobDetailDialog(
            job = job,
            onDismiss = { selectedJobForDetails = null },
            onApply = {
                handleApplyForJob(job)
                selectedJobForDetails = null
            },
            isApplying = isApplying,
            hasApplied = appliedJobIds.contains(job.jobId)
        )
    }
}

/**
 * Worker Home Content matching the approved mockup (Image 2).
 */
@Composable
private fun WorkerHomeContent(
    currentUser: User?,
    isHindi: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onVoiceSearchClick: () -> Unit,
    onFindWorkClick: () -> Unit,
    filteredJobs: List<Job>,
    appliedJobIds: List<String>,
    onApplyClick: (Job) -> Unit,
    onJobDetailsClick: (Job) -> Unit,
    myApplications: List<JobApplication>,
    onViewAllJobsClick: () -> Unit,
    onViewAllActiveWorkClick: () -> Unit,
    onOpenSchemesClick: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Top Bar with Logo, Location & Notifications
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

        // 2. Search Bar with Mic Icon
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ShramIcons.Search,
                        contentDescription = "Search",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = if (isHindi) "किस तरह का काम ढूंढ रहे हैं?" else "What kind of work are you looking for?",
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    IconButton(
                        onClick = onVoiceSearchClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = ShramIcons.Mic,
                            contentDescription = "Voice Search",
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // 3. Hero Banner ("आपका कौशल आपकी पहचान")
        item {
            Spacer(modifier = Modifier.height(4.dp))
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
                                colors = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7).copy(alpha = 0.5f))
                            ),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Content
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "आपका कौशल\nआपकी पहचान" else "Your Skill\nYour Identity",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B5E20),
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi)
                                    "यहाँ पाएं अपने लिए सही काम\nऔर बेहतर अवसर"
                                else
                                    "Find the right work\nand better opportunities",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32),
                                lineHeight = 14.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Solid green button: "काम खोजें ›"
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF047857))
                                        .clickable(onClick = onFindWorkClick)
                                        .padding(vertical = 8.dp, horizontal = 10.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = ShramIcons.Work,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isHindi) "काम खोजें ›" else "Find Work ›",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                        Text(
                                            text = if (isHindi) "नजदीक के काम देखें" else "View nearby jobs",
                                            fontSize = 8.sp,
                                            color = Color(0xFFD1FAE5)
                                        )
                                    }
                                }

                                // White outlined button: "बोलके काम खोजें [NEW] ›"
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(12.dp))
                                        .clickable(onClick = onVoiceSearchClick)
                                        .padding(vertical = 8.dp, horizontal = 10.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = ShramIcons.Mic,
                                                contentDescription = null,
                                                tint = Color(0xFF047857),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isHindi) "बोलके काम खोजें" else "Voice Search",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF047857)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                                    .background(Color(0xFFDC2626), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text("NEW", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        }
                                        Text(
                                            text = if (isHindi) "AI साथी से बात करें" else "Talk to AI Saathi",
                                            fontSize = 8.sp,
                                            color = Color(0xFF047857)
                                        )
                                    }
                                }
                            }
                        }

                        // Right illustration + Tagline
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isHindi) "काम\nअवसर\nअधिकार\nसाथ हमेशा" else "Work\nOpportunity\nRights\nWith You",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF047857),
                                textAlign = TextAlign.End,
                                lineHeight = 13.sp
                            )

                            com.example.shramsarthi.ui.components.WorkerAvatarIllustration(
                                modifier = Modifier.size(76.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. 4 Quick Circular Action Tiles (Image 2)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickCircleTile(
                    icon = ShramIcons.Work,
                    title = if (isHindi) "पास के काम" else "Nearby Jobs",
                    bgColor = Color(0xFFE0F2F1),
                    iconColor = Color(0xFF00695C),
                    onClick = onFindWorkClick
                )
                QuickCircleTile(
                    icon = ShramIcons.Group,
                    title = if (isHindi) "ठेकेदार खोजें" else "Contractors",
                    bgColor = Color(0xFFE3F2FD),
                    iconColor = Color(0xFF1565C0),
                    onClick = {
                        Toast.makeText(
                            context,
                            if (isHindi) "सत्यापित ठेकेदार सूची" else "Verified Contractors",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                QuickCircleTile(
                    icon = ShramIcons.LocationPin,
                    title = if (isHindi) "नक्शे पर देखें" else "Map View",
                    bgColor = Color(0xFFFFEBEE),
                    iconColor = Color(0xFFC62828),
                    onClick = {
                        Toast.makeText(
                            context,
                            if (isHindi) "GPS नक्शा दृश्य" else "Map View",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                QuickCircleTile(
                    icon = ShramIcons.Description,
                    title = if (isHindi) "सरकारी योजनाएं" else "Govt Schemes",
                    bgColor = Color(0xFFEDE7F6),
                    iconColor = Color(0xFF4527A0),
                    onClick = onOpenSchemesClick
                )
            }
        }

        // 5. Section: New Job Opportunities ("नए काम के अवसर")
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "नए काम के अवसर" else "New Job Opportunities",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isHindi) "सभी देखें ›" else "View All ›",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    modifier = Modifier.clickable(onClick = onViewAllJobsClick)
                )
            }
        }

        // Horizontal Carousel of Job Cards (Image 2)
        item {
            if (filteredJobs.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isHindi) "अभी कोई नया काम उपलब्ध नहीं है।" else "No new jobs available at the moment.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredJobs, key = { it.jobId }) { job ->
                        val hasApplied = appliedJobIds.contains(job.jobId)
                        JobOpportunityCard(
                            job = job,
                            hasApplied = hasApplied,
                            isHindi = isHindi,
                            onApplyClick = { onApplyClick(job) },
                            onClick = { onJobDetailsClick(job) }
                        )
                    }
                }
            }
        }

        // 6. Section: Active Work Tracker ("मेरे सक्रिय काम")
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "मेरे सक्रिय काम" else "My Active Work",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isHindi) "सभी देखें ›" else "View All ›",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    modifier = Modifier.clickable(onClick = onViewAllActiveWorkClick)
                )
            }
        }

        // Active Work Card matching Image 2
        item {
            val acceptedApplications = myApplications.filter { it.status == ApplicationStatus.ACCEPTED }
            if (acceptedApplications.isEmpty()) {
                // Default Active Work Card representation or prompt
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isHindi) "अभी कोई सक्रिय काम नहीं चल रहा है" else "No active work in progress",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi)
                                "ऊपर दी गई सूची से काम के लिए आवेदन करें।"
                            else
                                "Apply for jobs above to get engaged by verified contractors.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                acceptedApplications.forEach { app ->
                    ActiveWorkCard(
                        application = app,
                        isHindi = isHindi,
                        onViewDetails = onViewAllActiveWorkClick,
                        onMarkAttendance = {
                            Toast.makeText(
                                context,
                                if (isHindi) "आज की उपस्थिति दर्ज कर ली गई है" else "Today's attendance recorded",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onContactContractor = {
                            Toast.makeText(
                                context,
                                if (isHindi) "ठेकेदार से संपर्क किया जा रहा है" else "Connecting to contractor",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickCircleTile(
    icon: ImageVector,
    title: String,
    bgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(78.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            maxLines = 2
        )
    }
}

/**
 * Job Opportunity Card matching Image 2 horizontal list.
 */
@Composable
private fun JobOpportunityCard(
    job: Job,
    hasApplied: Boolean,
    isHindi: Boolean,
    onApplyClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Image Thumbnail + Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                when {
                    job.workerType.contains("Mason", ignoreCase = true) || job.title.contains("मिस्त्री", ignoreCase = true) -> {
                        com.example.shramsarthi.ui.components.MasonryThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    job.workerType.contains("Electrician", ignoreCase = true) || job.title.contains("बिजली", ignoreCase = true) -> {
                        com.example.shramsarthi.ui.components.ElectricianThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    job.workerType.contains("Painter", ignoreCase = true) || job.title.contains("पेंटर", ignoreCase = true) -> {
                        com.example.shramsarthi.ui.components.PainterThumbnail(modifier = Modifier.fillMaxSize())
                    }
                    else -> {
                        com.example.shramsarthi.ui.components.BuildingThumbnail(modifier = Modifier.fillMaxSize())
                    }
                }

                // Days ago badge on top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isHindi) "2 दिन पहले" else "2 days ago",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Card details
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = ShramIcons.BookmarkBorder,
                        contentDescription = "Save",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ShramIcons.LocationPin,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = job.location.ifBlank { if (isHindi) "ग्रेटर नोएडा" else "Greater Noida" },
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val rateVal = if (job.rate > 0.0) job.rate else 650.0
                Text(
                    text = "₹ ${rateVal.toInt()} - ₹${(rateVal * 1.25).toInt()} / " + if (isHindi) "दिन" else "day",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val count = if (job.workersRequired > 0) job.workersRequired else 2
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.Group, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isHindi) "$count लोग चाहिए" else "$count workers",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isHindi) "तुरंत शुरू" else "Immediate",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

/**
 * Active Work Card matching Image 2 layout with stats box and action buttons.
 */
@Composable
private fun ActiveWorkCard(
    application: JobApplication,
    isHindi: Boolean,
    onViewDetails: () -> Unit,
    onMarkAttendance: () -> Unit,
    onContactContractor: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Building Thumbnail
                com.example.shramsarthi.ui.components.BuildingThumbnail(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "रिहायशी बिल्डिंग निर्माण" else "Residential Construction",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isHindi) "चल रहा है" else "Ongoing",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            )
                        }
                    }

                    Text(
                        text = "ABC कंस्ट्रक्शन",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.LocationPin, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isHindi) "सेक्टर 150, ग्रेटर नोएडा" else "Sector 150, Greater Noida",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.Calendar, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isHindi) "10 सित 2026 – 25 सित 2026" else "10 Sep 2026 – 25 Sep 2026",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (isHindi) "₹700 / दिन (तय)" else "₹700 / day (Fixed)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar: "कार्य प्रगति" - "60%"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isHindi) "कार्य प्रगति" else "Work Progress",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Text(
                    text = "60%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF047857),
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Stats Box (Cyan/Light Blue)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0F7FA), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.Calendar, contentDescription = null, tint = Color(0xFF00695C), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = if (isHindi) "आज उपस्थिति" else "Today Attendance", fontSize = 10.sp, color = Color(0xFF00695C))
                            Text(text = "4 / 8 " + if (isHindi) "घंटे" else "hrs", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.CurrencyRupee, contentDescription = null, tint = Color(0xFF00695C), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = if (isHindi) "कुल भुगतान: ₹2,800" else "Total: ₹2,800", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = if (isHindi) "बकाया: ₹1,400" else "Due: ₹1,400", fontSize = 10.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Bottom Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Button 1: विवरण देखें
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F5F9))
                        .clickable(onClick = onViewDetails)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isHindi) "विवरण देखें" else "Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                // Button 2: उपस्थिति दर्ज करें
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF047857))
                        .clickable(onClick = onMarkAttendance)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isHindi) "उपस्थिति दर्ज करें" else "Mark Attendance",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Button 3: ठेकेदार से संपर्क
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFF047857), RoundedCornerShape(10.dp))
                        .clickable(onClick = onContactContractor)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ShramIcons.Call, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isHindi) "ठेकेदार से संपर्क" else "Call Contractor",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF047857)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Worker Profile Content matching Image 3 with verified badges, 4 stat tiles,
 * and clean single-language settings list.
 */
@Composable
private fun WorkerProfileContent(
    currentUser: User?,
    isHindi: Boolean,
    onSwitchRoleClick: () -> Unit,
    onOpenTermsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    var languageMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp)
    ) {
        // Top Bar
        ShramDashboardTopBar(
            title = if (isHindi) "श्रम सारथी" else "Shram Sarthi",
            subtitle = if (isHindi) "मजदूरों का साथी, बेहतर कल की ओर" else "Worker's Companion, Towards a Better Tomorrow",
            locationName = currentUser?.location?.ifBlank {
                if (isHindi) "ग्रेटर नोएडा" else "Greater Noida"
            } ?: if (isHindi) "ग्रेटर नोएडा" else "Greater Noida",
            notificationCount = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Worker Profile Card (Image 3)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Avatar with camera badge at bottom right
                    Box(modifier = Modifier.size(72.dp)) {
                        com.example.shramsarthi.ui.components.WorkerAvatarIllustration(
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .background(Color(0xFF047857), CircleShape)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ShramIcons.Camera,
                                contentDescription = "Camera",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUser?.fullName?.ifBlank {
                                        if (isHindi) "राहुल कुमार" else "Rahul Kumar"
                                    } ?: if (isHindi) "राहुल कुमार" else "Rahul Kumar",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = ShramIcons.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Edit Profile Pill Button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF047857), RoundedCornerShape(12.dp))
                                    .clickable {
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "प्रोफाइल संपादन" else "Edit Profile",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = ShramIcons.Edit, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isHindi) "प्रोफाइल संपादित करें" else "Edit Profile",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF047857)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = ShramIcons.Phone, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser?.mobileNumber?.let { "+91 $it" } ?: "+91 98765 43210",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = ShramIcons.CheckCircle,
                                contentDescription = "Verified",
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = ShramIcons.Work, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isHindi) "राजमिस्त्री (Mason)" else "Mason",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = ShramIcons.LocationPin, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser?.location?.ifBlank {
                                    if (isHindi) "सेक्टर 150, ग्रेटर नोएडा" else "Sector 150, Greater Noida"
                                } ?: if (isHindi) "सेक्टर 150, ग्रेटर नोएडा" else "Sector 150, Greater Noida",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4 Stat Tiles Row (Image 3)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatTileColumn(
                            icon = ShramIcons.Badge,
                            title = if (isHindi) "कौशल / ट्रेड" else "Trade Focus",
                            value = if (isHindi) "राजमिस्त्री" else "Mason"
                        )
                        ProfileStatTileColumn(
                            icon = ShramIcons.LocationPin,
                            title = if (isHindi) "कार्य क्षेत्र" else "Work Area",
                            value = if (isHindi) "ग्रेटर नोएडा" else "Greater Noida"
                        )
                        ProfileStatTileColumn(
                            icon = ShramIcons.CreditCard,
                            title = "UPI ID",
                            value = "rahul123@upi"
                        )
                        ProfileStatTileColumn(
                            icon = ShramIcons.Work,
                            title = if (isHindi) "अनुभव" else "Experience",
                            value = if (isHindi) "4 वर्ष" else "4 Years"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Group 1: Personal Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column {
                ProfileMenuItem(
                    icon = ShramIcons.Person,
                    title = if (isHindi) "मेरी जानकारी" else "My Details",
                    subtitle = if (isHindi) "नाम, फोन नंबर, प्रोफाइल विवरण" else "Name, phone, profile details",
                    onClick = {
                        Toast.makeText(context, if (isHindi) "मेरी जानकारी" else "My Details", Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = BorderSubtle)
                ProfileMenuItem(
                    icon = ShramIcons.LocationPin,
                    title = if (isHindi) "पता और कार्य क्षेत्र" else "Address & Work Area",
                    subtitle = if (isHindi) "वर्तमान लोकेशन, पसंदीदा कार्य क्षेत्र" else "Current location, preferred work area",
                    onClick = {
                        Toast.makeText(context, if (isHindi) "पता और कार्य क्षेत्र" else "Address & Work Area", Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = BorderSubtle)
                ProfileMenuItem(
                    icon = ShramIcons.Description,
                    title = if (isHindi) "कौशल और अनुभव" else "Skills & Experience",
                    subtitle = if (isHindi) "ट्रेड, अनुभव, अन्य विवरण" else "Trade, experience, other details",
                    onClick = {
                        Toast.makeText(context, if (isHindi) "कौशल और अनुभव" else "Skills & Experience", Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = BorderSubtle)
                ProfileMenuItem(
                    icon = ShramIcons.CreditCard,
                    title = if (isHindi) "भुगतान जानकारी" else "Payment Details",
                    subtitle = if (isHindi) "UPI ID, भुगतान प्राथमिकताएं" else "UPI ID, payout preferences",
                    onClick = {
                        Toast.makeText(context, if (isHindi) "भुगतान जानकारी" else "Payment Details", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Group 2: App Settings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column {
                // Language Selection Setting Item (Image 3)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { languageMenuExpanded = true }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFE0F2F1), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ShramIcons.Settings,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "भाषा बदलें" else "Change Language",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (isHindi) "हिन्दी / English" else "Hindi / English",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        // Dropdown Pill matching Image 3
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) "हिन्दी ⌵" else "English ⌵",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }

                            DropdownMenu(
                                expanded = languageMenuExpanded,
                                onDismissRequest = { languageMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(if (isHindi) "हिन्दी" else "Hindi", fontWeight = if (isHindi) FontWeight.Bold else FontWeight.Normal) },
                                    onClick = {
                                        LanguageManager.setLanguage(context, AppLanguage.HINDI)
                                        languageMenuExpanded = false
                                        Toast.makeText(context, "भाषा हिन्दी में बदली गई", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("English", fontWeight = if (!isHindi) FontWeight.Bold else FontWeight.Normal) },
                                    onClick = {
                                        LanguageManager.setLanguage(context, AppLanguage.ENGLISH)
                                        languageMenuExpanded = false
                                        Toast.makeText(context, "Language changed to English", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = BorderSubtle)

                // Role Switch
                ProfileMenuItem(
                    icon = ShramIcons.SwapHoriz,
                    title = if (isHindi) "भूमिका बदलें" else "Switch Role",
                    subtitle = if (isHindi) "मजदूर / ठेकेदार / बिल्डर / ग्राहक" else "Worker / Contractor / Builder / Customer",
                    onClick = onSwitchRoleClick
                )

                HorizontalDivider(color = BorderSubtle)

                // Terms & Policy
                ProfileMenuItem(
                    icon = ShramIcons.Shield,
                    title = if (isHindi) "गोपनीयता और नियम" else "Privacy & Terms",
                    subtitle = if (isHindi) "नियम एवं शर्तें, गोपनीयता नीति" else "Terms of Service, Privacy Policy",
                    onClick = onOpenTermsClick
                )

                HorizontalDivider(color = BorderSubtle)

                // Support & FAQs
                ProfileMenuItem(
                    icon = ShramIcons.HelpOutline,
                    title = if (isHindi) "सहायता और समर्थन" else "Help & Support",
                    subtitle = if (isHindi) "सामान्य प्रश्न, संपर्क करें" else "FAQs, Contact Us",
                    onClick = {
                        Toast.makeText(context, if (isHindi) "सहायता एवं समर्थन केंद्र" else "Help & Support Center", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Red Logout Row Card (Image 3)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = BorderStroke(1.dp, Color(0xFFFCA5A5).copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLogoutClick)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ShramIcons.Logout,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isHindi) "लॉग आउट" else "Log Out",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }

                    Icon(
                        imageVector = ShramIcons.ArrowForward,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatTileColumn(
    icon: ImageVector,
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF047857),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = title,
            fontSize = 9.sp,
            color = Color(0xFF065F46),
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF064E3B),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFE0F2F1), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
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

            Icon(
                imageVector = ShramIcons.ArrowForward,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

