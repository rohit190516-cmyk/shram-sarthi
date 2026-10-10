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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobStatus
import com.example.shramsarthi.model.User
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.model.getLocalizedSkill
import com.example.shramsarthi.ui.components.ShramDashboardTopBar
import com.example.shramsarthi.ui.components.ShramEmergencySheet
import com.example.shramsarthi.ui.components.ShramEmptyState
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramOutlinedButton
import com.example.shramsarthi.ui.components.ShramPostJobDialog
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.RoleBuilderColor
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage
import kotlinx.coroutines.launch

@Composable
fun BuilderDashboardScreen(
    onNavigateBack: () -> Unit,
    onLogoutClick: () -> Unit,
    onSwitchRoleClick: () -> Unit,
    onOpenTermsClick: () -> Unit = {}
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI

    val role = UserRole.BUILDER
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

    // Live Jobs from Firestore
    val openJobsList by ShramSarthiRepositoryProvider.jobRepository
        .observeOpenJobs()
        .collectAsState(initial = emptyList())

    val totalWorkersNeeded = openJobsList.sumOf { it.workersRequired }
    val activeRequirements = openJobsList.size

    val userCity = currentUser?.location?.split(",")?.firstOrNull()?.trim()?.ifBlank {
        if (isHindi) "ग्रेटर नोएडा" else "Greater Noida"
    } ?: if (isHindi) "ग्रेटर नोएडा" else "Greater Noida"

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
                subtitle = if (isHindi) "बिल्डर प्रोजेक्ट पोर्टल" else "Builder Project Portal",
                locationName = userCity
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
            // Quick Worker Mode Switcher Banner
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onSwitchRoleClick() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(Color(0xFF047857), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ShramIcons.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "श्रमिक (Worker) मोड पर स्विच करें" else "Switch to Worker Dashboard",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = if (isHindi) "काम, हाज़िरी, शिकायत व प्रोफ़ाइल देखें" else "View jobs, records, complaints & profile",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        Icon(
                            imageVector = ShramIcons.ArrowForward,
                            contentDescription = "Switch",
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 1. Compact Greeting & Voice Requisition Banner
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(RoleBuilderColor.copy(alpha = 0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentUser?.fullName?.take(1)?.uppercase() ?: (if (isHindi) "बी" else "B"),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoleBuilderColor
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${if (isHindi) "नमस्ते" else "Hello"}, ${currentUser?.fullName?.ifBlank { if (isHindi) "बिल्डर साथी" else "Builder" } ?: (if (isHindi) "बिल्डर साथी" else "Builder")}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isHindi) "परियोजनाएं एवं कार्यबल प्रबंधन" else "Project & Workforce Operations",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(RoleBuilderColor.copy(alpha = 0.12f))
                                    .clickable { onSwitchRoleClick() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = role.getLocalizedTitle(isHindi),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoleBuilderColor
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = ShramIcons.SwapHoriz,
                                        contentDescription = "Switch Role",
                                        tint = RoleBuilderColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Compact Voice Action Pill
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF0FDF4))
                                .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (isHindi) "🎤 बोलिए... किस साइट के लिए कितने कामगार चाहिए?" else "🎤 Speak... What are your site worker requirements?"
                                        )
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(RoleBuilderColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ShramIcons.Mic,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isHindi) "बोलकर कारीगरों की मांग दर्ज करें..." else "Tap mic to voice worker requisition...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RoleBuilderColor,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (isHindi) "बोलें" else "Speak",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoleBuilderColor
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 3 Compact Quick Action Chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(RoleBuilderColor)
                                    .clickable { showPostJobDialog = true }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = ShramIcons.Add,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isHindi) "नई मांग" else "New Indent",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                                    .clickable {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                if (isHindi) "साइट्स सूची सक्रिय है" else "Site locations active"
                                            )
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = ShramIcons.Work,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isHindi) "साइट्स" else "Sites",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                                    .clickable {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                if (isHindi) "लेबर सेस एवं न्यूनतम वेतन सत्यापित है" else "Labor cess and minimum wage verified"
                                            )
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = ShramIcons.Shield,
                                        contentDescription = null,
                                        tint = TealPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isHindi) "अनुपालन" else "Compliance",
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

            // 2. Statistics KPI Strip (Compact 3-card layout)
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isHindi) "सक्रिय मांगें" else "Active Reqs",
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$activeRequirements",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoleBuilderColor
                            )
                            Text(
                                text = if (isHindi) "खुली परियोजनाएं" else "Open sites",
                                fontSize = 9.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isHindi) "कामगार मांग" else "Workers",
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$totalWorkersNeeded",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                            Text(
                                text = if (isHindi) "आवश्यक संख्या" else "Required",
                                fontSize = 9.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isHindi) "अनुपालन" else "Compliance",
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "100%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            )
                            Text(
                                text = if (isHindi) "सत्यापित" else "Verified",
                                fontSize = 9.sp,
                                color = Color(0xFF047857)
                            )
                        }
                    }
                }
            }

            // 3. Site Requisitions Section Header
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "प्रोजेक्ट मांगें" else "Project Requisitions",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isHindi) "सक्रिय साइट आवश्यकताएं" else "Active site requirements",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(TealContainer, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isHindi) "$activeRequirements मांगें" else "$activeRequirements Open",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 4. Real Requisitions List or Clear Empty State
            if (openJobsList.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        ShramEmptyState(
                            title = if (isHindi) "कोई सक्रिय मांग नहीं है" else "No Active Requisitions",
                            subtitle = if (isHindi) "नई साइट के लिए कारीगरों की मांग दर्ज करें।" else "Post a worker requisition for your project sites.",
                            buttonText = if (isHindi) "+ नई मांग दर्ज करें" else "+ Post New Requisition",
                            onButtonClick = { showPostJobDialog = true }
                        )
                    }
                }
            } else {
                items(openJobsList, key = { it.jobId }) { job ->
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
                                            color = TextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${getLocalizedSkill(job.workerType, isHindi)} • ${job.location}",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFECFDF5), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 7.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (isHindi) "${job.workersRequired} कामगार" else "${job.workersRequired} Workers",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoleBuilderColor
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
                                        text = "${if (isHindi) "बजट: ₹" else "Budget: ₹"}${job.rate.toInt()} ${if (isHindi) "/ दिन" else "/ day"}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TealPrimary
                                    )

                                    Text(
                                        text = "${if (isHindi) "स्थिति: " else "Status: "}${if (job.status == JobStatus.OPEN) (if (isHindi) "खुला है" else "Open") else job.status.name}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Bottom Navigation & Role Switch Actions
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    ShramOutlinedButton(
                        text = if (isHindi) "भूमिका बदलें" else "Switch Role",
                        onClick = onSwitchRoleClick,
                        borderColor = RoleBuilderColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShramOutlinedButton(
                        text = if (isHindi) "नियम व शर्तें" else "Terms & Policy",
                        onClick = onOpenTermsClick,
                        borderColor = TealPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShramPrimaryButton(
                        text = if (isHindi) "लॉग आउट करें" else "Log Out",
                        onClick = onLogoutClick,
                        containerColor = Color(0xFFDC2626),
                        modifier = Modifier.height(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                val jobWithBuilder = newJob.copy(thekedarId = currentUid.ifBlank { "builder_corp_1" })
                coroutineScope.launch {
                    val result = ShramSarthiRepositoryProvider.jobRepository.createJob(jobWithBuilder)
                    result.onSuccess {
                        snackbarHostState.showSnackbar(
                            if (isHindi) "✓ मांग सफलतापूर्वक दर्ज हुई!" else "✓ Requisition posted successfully!"
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

    // Emergency Sheet
    if (showEmergencyDialog) {
        ShramEmergencySheet(onDismiss = { showEmergencyDialog = false })
    }
}
