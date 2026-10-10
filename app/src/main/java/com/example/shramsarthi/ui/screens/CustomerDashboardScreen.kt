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
import com.example.shramsarthi.ui.theme.RoleCustomerColor
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun CustomerDashboardScreen(
    onNavigateBack: () -> Unit,
    onLogoutClick: () -> Unit,
    onSwitchRoleClick: () -> Unit,
    onOpenTermsClick: () -> Unit = {}
) {
    val isHindi = LocalAppLanguage.current == AppLanguage.HINDI
    val role = UserRole.CUSTOMER
    val currentUid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: ""
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // User Profile state from Firestore
    var currentUser by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(currentUid) {
        if (currentUid.isNotBlank()) {
            val userResult = ShramSarthiRepositoryProvider.userRepository.getUser(currentUid)
            currentUser = userResult.getOrNull()
        }
    }

    var showRequestDialog by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }
    var selectedCategoryForRequest by remember { mutableStateOf(if (isHindi) "प्लंबर" else "Plumber") }

    // Live Jobs requested by this Customer from Firestore
    val myRequests by ShramSarthiRepositoryProvider.jobRepository
        .observeJobsByThekedar(currentUid)
        .collectAsState(initial = emptyList())

    val customerCategories = if (isHindi) {
        listOf(
            Pair("प्लंबर", "₹300 - ₹500 मानक विज़िट"),
            Pair("इलेक्ट्रीशियन", "₹350 - ₹600 मानक विज़िट"),
            Pair("पेंटर", "₹700 - ₹850 / दिन"),
            Pair("बढ़ई", "₹750 - ₹950 / दिन"),
            Pair("राजमिस्त्री", "₹800 - ₹950 / दिन"),
            Pair("सहायक मजदूर", "₹500 - ₹600 / दिन")
        )
    } else {
        listOf(
            Pair("Plumber", "₹300 - ₹500 standard visit"),
            Pair("Electrician", "₹350 - ₹600 standard visit"),
            Pair("Painter", "₹700 - ₹850 / day"),
            Pair("Carpenter", "₹750 - ₹950 / day"),
            Pair("Mason", "₹800 - ₹950 / day"),
            Pair("General Helper", "₹500 - ₹600 / day")
        )
    }

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
                subtitle = if (isHindi) "उपभोक्ता पोर्टल" else "Customer Portal"
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
            // Friendly Assistant Card for Customer
            item {
                Spacer(modifier = Modifier.height(6.dp))
                ShramAssistantCard(
                    userName = currentUser?.fullName ?: if (isHindi) "ग्राहक साथी" else "Customer",
                    greetingTitle = if (isHindi) "नमस्ते" else "Welcome",
                    quickAction1 = Pair(if (isHindi) "कारीगर बुलाएं" else "Book Worker", ShramIcons.Add),
                    quickAction2 = Pair(if (isHindi) "रेट कार्ड" else "Rate Card", ShramIcons.CurrencyRupee),
                    quickAction3 = Pair(if (isHindi) "मदद" else "Help", ShramIcons.Support),
                    onActionClick = { action ->
                        when {
                            action == "कारीगर बुलाएं" || action == "Book Worker" -> {
                                selectedCategoryForRequest = if (isHindi) "प्लंबर" else "Plumber"
                                showRequestDialog = true
                            }
                            action == "रेट कार्ड" || action == "Rate Card" -> coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    if (isHindi) "मानक दरें नीचे दी गई हैं" else "Standard rates are shown below"
                                )
                            }
                            action == "मदद" || action == "Help" -> showEmergencyDialog = true
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
                                if (isHindi) "🎤 बोलिए... घर के लिए प्लंबर, इलेक्ट्रीशियन या पेंटर चाहिए?"
                                else "🎤 Speak... Need a plumber, electrician, or painter?"
                            )
                        }
                    },
                    caption = if (isHindi) "बोलकर पूछें" else "Voice Search",
                    subtitle = if (isHindi) "माइक दबाएं और काम की आवश्यकता बताएं" else "Tap mic to speak your requirement"
                )
            }

            // 3 Compact Service Cards
            item {
                ShramServiceCardsRow(
                    card1Title = if (isHindi) "कारीगर बुलाएं" else "Book Worker",
                    card1Sub = if (isHindi) "बुकिंग करें" else "Book service",
                    card1Icon = ShramIcons.Add,
                    onCard1Click = {
                        selectedCategoryForRequest = if (isHindi) "प्लंबर" else "Plumber"
                        showRequestDialog = true
                    },
                    card2Title = if (isHindi) "रेट कार्ड" else "Rate Card",
                    card2Sub = if (isHindi) "मानक दरें" else "Standard rates",
                    card2Icon = ShramIcons.CurrencyRupee,
                    onCard2Click = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (isHindi) "पारदर्शी सेवा दरें प्रदर्शित हैं" else "Transparent service rates displayed"
                            )
                        }
                    },
                    card3Title = if (isHindi) "मदद" else "Helpline",
                    card3Sub = if (isHindi) "सहायता केंद्र" else "Support",
                    card3Icon = ShramIcons.Support,
                    onCard3Click = { showEmergencyDialog = true }
                )
            }

            // Standard Service Categories Header
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
                            text = if (isHindi) "घरेलू सेवाएं" else "Home Services",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isHindi) "सत्यापित स्थानीय कारीगर • पारदर्शी न्यूनतम दरें" else "Verified local technicians • Transparent base rates",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // Categories List
            items(customerCategories) { (catTitle, catPricing) ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                selectedCategoryForRequest = catTitle
                                showRequestDialog = true
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = catTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = catPricing,
                                    fontSize = 12.sp,
                                    color = TealPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(RoleCustomerColor)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (isHindi) "बुलाएं" else "Book",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // My Active Requests Header
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "मेरे अनुरोध" else "My Requests",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${myRequests.size} " + (if (isHindi) "अनुरोध" else "requests"),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            if (myRequests.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isHindi) "अभी कोई सक्रिय अनुरोध नहीं है" else "No active requests currently",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = if (isHindi) "ऊपर दी गई किसी भी सेवा पर टैप करके कारीगर बुलाएं" else "Tap any service above to book a technician",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            } else {
                items(myRequests, key = { it.jobId }) { job ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
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
                                        .background(TealContainer, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (job.status == JobStatus.OPEN) (if (isHindi) "सक्रिय" else "Active") else job.status.name,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TealPrimary
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
                        borderColor = RoleCustomerColor
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

    // Request Technician Dialog
    if (showRequestDialog) {
        ShramPostJobDialog(
            initialCategory = selectedCategoryForRequest,
            onDismiss = { showRequestDialog = false },
            onPostJob = { newJob ->
                showRequestDialog = false
                val jobWithCustomer = newJob.copy(
                    thekedarId = currentUid.ifBlank { "customer_individual" },
                    title = if (isHindi) "घरेलू सेवा: ${newJob.workerType}" else "Home Service: ${newJob.workerType}"
                )
                coroutineScope.launch {
                    val result = ShramSarthiRepositoryProvider.jobRepository.createJob(jobWithCustomer)
                    result.onSuccess {
                        snackbarHostState.showSnackbar(
                            if (isHindi) "✓ कारीगर बुकिंग अनुरोध सफलतापूर्वक दर्ज हुआ!" else "✓ Service request submitted successfully!"
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
