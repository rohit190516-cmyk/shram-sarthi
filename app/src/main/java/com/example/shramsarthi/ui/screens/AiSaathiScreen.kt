package com.example.shramsarthi.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.data.ai.AiIntentType
import com.example.shramsarthi.data.ai.AiSaathiResponse
import com.example.shramsarthi.data.ai.GeminiAiSaathiService
import com.example.shramsarthi.data.ai.WorkRequestDraft
import com.example.shramsarthi.data.knowledge.GovernmentSchemeInfo
import com.example.shramsarthi.data.knowledge.LabourRightInfo
import com.example.shramsarthi.data.location.LocationService
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.User
import com.example.shramsarthi.ui.components.BrandLogoBadge
import com.example.shramsarthi.ui.components.ShramCompactJobCard
import com.example.shramsarthi.ui.components.ShramEmptyState
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramJobDetailDialog
import com.example.shramsarthi.ui.components.ShramOutlinedButton
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.SaffronContainer
import com.example.shramsarthi.ui.theme.SuccessGreen
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TealPrimaryDark
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.voice.VoiceSpeechManager
import kotlinx.coroutines.launch

@Composable
fun AiSaathiScreen(
    onNavigateBack: () -> Unit,
    onJobApplied: (Job) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    // Services
    val locationService = remember { LocationService(context) }
    val aiService = remember {
        GeminiAiSaathiService(
            context = context,
            jobRepository = ShramSarthiRepositoryProvider.jobRepository,
            locationService = locationService
        )
    }

    // Current User
    var currentUser by remember { mutableStateOf<User?>(null) }
    val currentUid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId() ?: ""
    LaunchedEffect(currentUid) {
        if (currentUid.isNotBlank()) {
            currentUser = ShramSarthiRepositoryProvider.userRepository.getUser(currentUid).getOrNull()
        }
    }

    val language = com.example.shramsarthi.util.LocalAppLanguage.current
    val isHindi = language == com.example.shramsarthi.util.AppLanguage.HINDI

    // State
    var textInput by remember { mutableStateOf("") }
    var currentResponse by remember { mutableStateOf<AiSaathiResponse?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var selectedJobForDetails by remember { mutableStateOf<Job?>(null) }

    // Speech Engine with safe self-reference
    var speechManagerRef by remember { mutableStateOf<VoiceSpeechManager?>(null) }
    val speechManager = remember {
        VoiceSpeechManager(context) { finalTranscript ->
            textInput = finalTranscript
            coroutineScope.launch {
                isProcessing = true
                val resp = aiService.processQuery(finalTranscript, currentUser)
                currentResponse = resp
                isProcessing = false
                speechManagerRef?.speak(resp.spokenHindi, if (isHindi) "hi-IN" else "en-IN")
            }
        }.also { speechManagerRef = it }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechManager.release()
        }
    }

    val isListening by speechManager.isListening.collectAsState()
    val isSpeaking by speechManager.isSpeaking.collectAsState()
    val partialTranscript by speechManager.partialTranscript.collectAsState()
    val speechError by speechManager.errorMessage.collectAsState()

    // Permission Launchers
    var hasMicPermission by remember { mutableStateOf(speechManager.hasMicrophonePermission()) }
    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasMicPermission = isGranted
        if (isGranted) {
            speechManager.startListening(if (isHindi) "hi-IN" else "en-IN")
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    if (isHindi) "माइक अनुमति अस्वीकृत हुई। आप नीचे लिखकर भी सवाल पूछ सकते हैं।" else "Microphone permission denied. You can type your query below."
                )
            }
        }
    }

    var hasLocationPermission by remember { mutableStateOf(locationService.hasLocationPermission()) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        hasLocationPermission = perms.values.any { it }
        if (hasLocationPermission) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("स्थान अनुमति प्राप्त हुई। निकटतम काम खोजे जा रहे हैं।")
            }
        }
    }

    // Trigger query processing helper
    fun executeQuery(query: String) {
        if (query.isBlank()) return
        speechManager.stopSpeaking()
        speechManager.stopListening()
        coroutineScope.launch {
            isProcessing = true
            val resp = aiService.processQuery(query, currentUser)
            currentResponse = resp
            isProcessing = false
            speechManager.speak(resp.spokenHindi)
        }
    }

    // Pulsing animation for listening state
    val infiniteTransition = rememberInfiniteTransition(label = "VoicePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            speechManager.stopSpeaking()
                            speechManager.stopListening()
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = ShramIcons.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    BrandLogoBadge(size = 32)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "AI साथी" else "AI Saathi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimaryDark
                        )
                        Text(
                            text = if (isHindi) "बोलकर खोजें काम, योजनाएं व अधिकार" else "Voice search for jobs, schemes & rights",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Permission Warning Card (if Mic permission missing)
            if (!hasMicPermission) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SaffronContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SaffronAccent.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "माइक अनुमति आवश्यक है",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "आवाज़ से खोजने के लिए माइक्रोफ़ोन एक्सेस चालू करें।",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        ShramPrimaryButton(
                            text = "अनुमति दें",
                            onClick = { micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                            modifier = Modifier.height(36.dp)
                        )
                    }
                }
            }

            // Location Access Hint
            if (!hasLocationPermission) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(TealContainer.copy(alpha = 0.5f))
                        .clickable {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ShramIcons.LocationPin,
                        contentDescription = null,
                        tint = TealPrimaryDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GPS स्थान चालू करें ताकि 10-25 किमी के नजदीकी काम मिलें",
                        fontSize = 11.sp,
                        color = TealPrimaryDark,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Central Voice Interaction Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Status Indicator Badge
                    val statusText = when {
                        isProcessing -> if (isHindi) "सारथी सोच रहा है..." else "AI Saathi is thinking..."
                        isListening -> if (isHindi) "सुन रहा हूँ... बोलिए!" else "Listening... speak now!"
                        isSpeaking -> if (isHindi) "सारथी बोल रहा है..." else "AI Saathi is speaking..."
                        else -> if (isHindi) "माइक दबाकर अपनी भाषा में बोलें" else "Tap microphone to speak"
                    }
                    val statusColor = when {
                        isProcessing -> TealPrimary
                        isListening -> SaffronAccent
                        isSpeaking -> SuccessGreen
                        else -> TextMuted
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(statusColor.copy(alpha = 0.12f))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = statusText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Big Interactive Microphone Button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(110.dp)
                    ) {
                        // Pulsing outer ripple when listening
                        if (isListening) {
                            Box(
                                modifier = Modifier
                                    .size(105.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(SaffronAccent.copy(alpha = 0.25f))
                            )
                        }

                        // Core Button
                        val micBgColor = when {
                            isListening -> SaffronAccent
                            isSpeaking -> SuccessGreen
                            isProcessing -> TealPrimaryDark
                            else -> TealPrimary
                        }

                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(micBgColor)
                                .clickable {
                                    if (isListening) {
                                        speechManager.stopListening()
                                    } else {
                                        if (hasMicPermission) {
                                            speechManager.startListening(if (isHindi) "hi-IN" else "en-IN")
                                        } else {
                                            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(36.dp),
                                    strokeWidth = 3.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (isSpeaking) ShramIcons.VolumeUp else ShramIcons.Mic,
                                    contentDescription = "Microphone",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Audio Playback Controls (Stop)
                    AnimatedVisibility(visible = isSpeaking) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ShramOutlinedButton(
                                text = "आवाज़ रोकें (Stop)",
                                onClick = { speechManager.stopSpeaking() },
                                modifier = Modifier.height(36.dp)
                            )
                        }
                    }

                    // Live Speech Transcript Bubble
                    if (partialTranscript.isNotBlank() || isListening) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(BackgroundLight)
                                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = if (partialTranscript.isNotBlank()) "🎤 \"$partialTranscript\"" else "🎤 आवाज़ सुन रहे हैं...",
                                fontSize = 14.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Speech Error Message
                    if (speechError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = speechError ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Editable Text Fallback Field (For noisy sites or manual editing)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = {
                                Text(
                                    text = "या यहाँ लिखकर पूछें (उदा. राजमिस्त्री काम)",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealPrimary,
                                unfocusedBorderColor = BorderSubtle,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (textInput.isNotBlank()) TealPrimary else Color.LightGray)
                                .clickable(enabled = textInput.isNotBlank()) {
                                    val q = textInput
                                    textInput = ""
                                    executeQuery(q)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ShramIcons.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Vernacular Quick Suggestions Pills
                    QuickPromptChips(onChipClick = { executeQuery(it) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RESULTS DISPLAY SECTION
            val resp = currentResponse
            if (resp != null) {
                ResponseDisplaySection(
                    response = resp,
                    onJobApply = { job ->
                        onJobApplied(job)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("काम '${job.title}' हेतु आवेदन जमा कर दिया गया।")
                        }
                    },
                    onJobDetails = { job -> selectedJobForDetails = job },
                    onCopyDraft = { draftText ->
                        clipboardManager.setText(AnnotatedString(draftText))
                        Toast.makeText(context, "आवेदन कॉपी हो गया!", Toast.LENGTH_SHORT).show()
                    },
                    onShareDraft = { draftText ->
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, draftText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "कार्य आवेदन साझा करें"))
                    },
                    onSearchWithDraftCriteria = { trade, location ->
                        val q = "$location में $trade का काम खोजें"
                        executeQuery(q)
                    },
                    onOpenUrl = { url ->
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "लिंक खोलने में असमर्थ", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onDialPhone = { phone ->
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                )
            } else {
                // Initial Guide Card
                WelcomeGuideCard()
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Job Detail Modal Sheet
    selectedJobForDetails?.let { job ->
        ShramJobDetailDialog(
            job = job,
            onDismiss = { selectedJobForDetails = null },
            onApply = {
                selectedJobForDetails = null
                onJobApplied(job)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("आवेदन सफलतापूर्वक भेजा गया!")
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickPromptChips(onChipClick: (String) -> Unit) {
    val prompts = listOf(
        "ग्रेटर नोएडा में राजमिस्त्री काम",
        "ई-श्रम कार्ड के फायदे",
        "ठेकेदार पैसे न दे तो क्या करें?",
        "काम का औपचारिक आवेदन बनाओ",
        "न्यूनतम मजदूरी नियम"
    )

    val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (isHindi) "शीघ्र प्रश्न:" else "Quick Prompts:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            prompts.forEach { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TealContainer.copy(alpha = 0.6f))
                        .clickable { onChipClick(prompt) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = prompt,
                        fontSize = 11.sp,
                        color = TealPrimaryDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeGuideCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "नमस्ते! मैं आपका श्रम सारथी साथी हूँ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimaryDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "मैं असंगठित और निर्माण क्षेत्र के श्रमिक भाइयों के लिए विशेष रूप से बनाया गया हूँ।",
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(12.dp))

            FeatureBullet(
                icon = ShramIcons.Work,
                title = "सत्यापित काम खोजें",
                desc = "फायरस्टोर पर मौजूद ठेकेदारों के असली काम और दैनिक दरें देखें।"
            )
            Spacer(modifier = Modifier.height(8.dp))
            FeatureBullet(
                icon = ShramIcons.Shield,
                title = "सरकारी योजनाएं (e-Shram, पेंशन)",
                desc = "ई-श्रम, पीएम मान-धन, विश्वकर्मा योजना की पात्रता व लिंक।"
            )
            Spacer(modifier = Modifier.height(8.dp))
            FeatureBullet(
                icon = ShramIcons.Scale,
                title = "मजदूरी कानून व शिकायत",
                desc = "न्यूनतम मजदूरी, ओवरटाइम का 2 गुना भुगतान और समाधान पोर्टल।"
            )
            Spacer(modifier = Modifier.height(8.dp))
            FeatureBullet(
                icon = ShramIcons.Description,
                title = "कार्य उपलब्धता आवेदन",
                desc = "ठेकेदारों को भेजने के लिए सम्मानजनक पेशेवर बायोडाटा बनाएं।"
            )
        }
    }
}

@Composable
private fun FeatureBullet(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TealPrimary,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun ResponseDisplaySection(
    response: AiSaathiResponse,
    onJobApply: (Job) -> Unit,
    onJobDetails: (Job) -> Unit,
    onCopyDraft: (String) -> Unit,
    onShareDraft: (String) -> Unit,
    onSearchWithDraftCriteria: (String, String) -> Unit,
    onOpenUrl: (String) -> Unit,
    onDialPhone: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Main Response Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = response.displayTitleHindi,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimaryDark,
                        modifier = Modifier.weight(1f)
                    )
                    response.lastVerifiedDate?.let { date ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TealContainer)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "सत्यापित: $date",
                                fontSize = 10.sp,
                                color = TealPrimaryDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = response.displayTextHindi,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )

                response.legalDisclaimer?.let { disclaimer ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BackgroundLight)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "⚖️ $disclaimer",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )
                    }
                }

                response.officialSourceUrl?.let { url ->
                    val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI
                    Spacer(modifier = Modifier.height(12.dp))
                    ShramPrimaryButton(
                        text = if (isHindi) "आधिकारिक पोर्टल पर जाएं" else "Official Portal",
                        onClick = { onOpenUrl(url) },
                        modifier = Modifier.height(42.dp)
                    )
                }
            }
        }

        val isHindi = com.example.shramsarthi.util.LocalAppLanguage.current == com.example.shramsarthi.util.AppLanguage.HINDI

        // 1. MATCHED JOBS CARDS
        if (response.intentType == AiIntentType.FIND_WORK) {
            if (response.matchedJobs.isEmpty()) {
                ShramEmptyState(
                    title = if (isHindi) "इस स्थान पर कोई सक्रिय काम नहीं मिला" else "No active jobs found for this location",
                    subtitle = if (isHindi) "फायरस्टोर डेटाबेस में इस ट्रेड या शहर का कोई खाली पद नहीं है। दायरा बढ़ाकर देखें।" else "No openings found in Firestore for this trade or city.",
                    buttonText = if (isHindi) "सभी उपलब्ध काम देखें" else "View All Available Jobs",
                    onButtonClick = { /* User can browse */ }
                )
            } else {
                Text(
                    text = if (isHindi) "उपलब्ध नौकरियां (${response.matchedJobs.size}):" else "Available Jobs (${response.matchedJobs.size}):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimaryDark
                )
                response.matchedJobs.forEach { job ->
                    ShramCompactJobCard(
                        job = job,
                        hasApplied = false,
                        onApplyClick = { onJobApply(job) },
                        onViewDetailsClick = { onJobDetails(job) }
                    )
                }
            }
        }

        // 2. MATCHED GOVERNMENT SCHEMES
        if (response.intentType == AiIntentType.GOVERNMENT_SCHEME && response.matchedSchemes.isNotEmpty()) {
            Text(
                text = if (isHindi) "संबंधित सरकारी योजनाएं:" else "Government Schemes:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimaryDark
            )
            response.matchedSchemes.forEach { scheme ->
                GovernmentSchemeDetailCard(scheme = scheme, onOpenUrl = onOpenUrl, onDialPhone = onDialPhone)
            }
        }

        // 3. MATCHED LABOUR RIGHTS
        if (response.intentType == AiIntentType.LABOUR_RIGHTS && response.matchedRights.isNotEmpty()) {
            Text(
                text = if (isHindi) "कानूनी अधिकार व समाधान:" else "Statutory Protections:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimaryDark
            )
            response.matchedRights.forEach { right ->
                LabourRightDetailCard(right = right, onOpenUrl = onOpenUrl, onDialPhone = onDialPhone)
            }
        }

        // 4. WORK REQUEST DRAFT CARD
        response.workRequestDraft?.let { draft ->
            WorkRequestDraftCard(
                draft = draft,
                onCopy = { onCopyDraft(draft.hindiText) },
                onShare = { onShareDraft(draft.hindiText) },
                onSearchJobs = {
                    onSearchWithDraftCriteria(draft.trade, draft.preferredLocation)
                }
            )
        }
    }
}

@Composable
private fun GovernmentSchemeDetailCard(
    scheme: GovernmentSchemeInfo,
    onOpenUrl: (String) -> Unit,
    onDialPhone: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = scheme.nameHindi,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimaryDark
            )
            Text(
                text = scheme.ministryOrDepartment,
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "मुख्य लाभ (Verified Benefits):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            scheme.verifiedBenefitsHindi.forEach { benefit ->
                Text(
                    text = "• $benefit",
                    fontSize = 12.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "पात्रता (Eligibility):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            scheme.eligibilityCriteriaHindi.forEach { crit ->
                Text(
                    text = "✓ $crit",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "आवश्यक दस्तावेज (Required Documents):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = scheme.requiredDocumentsHindi.joinToString(", "),
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ShramPrimaryButton(
                    text = "आवेदन पोर्टल लिंक",
                    onClick = { onOpenUrl(scheme.officialApplicationUrl) },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                )
                if (scheme.officialHelpline.isNotBlank()) {
                    ShramOutlinedButton(
                        text = "हेल्पलाइन डायल",
                        onClick = { onDialPhone(scheme.officialHelpline.filter { it.isDigit() }) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LabourRightDetailCard(
    right: LabourRightInfo,
    onOpenUrl: (String) -> Unit,
    onDialPhone: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = right.titleHindi,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimaryDark
            )
            Text(
                text = "कानून: ${right.applicableLaw}",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))

            right.keyPointsHindi.forEach { point ->
                Text(
                    text = "• $point",
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ShramPrimaryButton(
                    text = "शिकायत पोर्टल (SAMADHAN)",
                    onClick = { onOpenUrl(right.officialLink) },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                )
                if (right.officialHelpline.isNotBlank()) {
                    ShramOutlinedButton(
                        text = "श्रम हेल्पलाइन",
                        onClick = { onDialPhone(right.officialHelpline.filter { it.isDigit() }) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkRequestDraftCard(
    draft: WorkRequestDraft,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onSearchJobs: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFDFF)),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "तैयार कार्य उपलब्धता आवेदन",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimaryDark
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SaffronContainer)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "समीक्षा हेतु तैयार",
                        fontSize = 10.sp,
                        color = SaffronAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = draft.hindiText,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShramOutlinedButton(
                    text = "कॉपी करें",
                    onClick = onCopy,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                )
                ShramPrimaryButton(
                    text = "शेयर करें (WhatsApp)",
                    onClick = onShare,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            ShramOutlinedButton(
                text = "इस ट्रेड के उपलब्ध काम खोजें",
                onClick = onSearchJobs,
                modifier = Modifier.height(38.dp)
            )
        }
    }
}
