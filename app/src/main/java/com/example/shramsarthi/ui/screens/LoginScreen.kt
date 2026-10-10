package com.example.shramsarthi.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.ui.components.BrandLogoBadge
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderStrong
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.SaffronAccent
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LoginScreen(
        onNavigateBack = onNavigateBack,
        onLoginSuccessWithRole = { _, _ -> onLoginSuccess() },
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccessWithRole: (UserRole?) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LoginScreen(
        onNavigateBack = onNavigateBack,
        onLoginSuccessWithRole = { role, _ -> onLoginSuccessWithRole(role) },
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccessWithRole: (userRole: UserRole?, isNewUser: Boolean) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val currentLanguage = com.example.shramsarthi.util.LocalAppLanguage.current
    val isHindi = currentLanguage == com.example.shramsarthi.util.AppLanguage.HINDI

    var mobileNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    // Authentication States
    var isOtpSent by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var resendTimer by remember { mutableIntStateOf(60) }

    var mobileError by remember { mutableStateOf<String?>(null) }
    var otpError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }
    var successNotice by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Resend countdown timer effect
    LaunchedEffect(isOtpSent, resendTimer) {
        if (isOtpSent && resendTimer > 0) {
            delay(1000L)
            resendTimer--
        }
    }

    fun formatFirebaseError(throwable: Throwable): String {
        val message = throwable.localizedMessage ?: throwable.message ?: "Authentication error"
        return when {
            throwable is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ||
                    message.contains("ERROR_INVALID_VERIFICATION_CODE", ignoreCase = true) ||
                    message.contains("invalid-verification-code", ignoreCase = true) ||
                    message.contains("invalid credential", ignoreCase = true) -> {
                "गलत ओटीपी कोड (Invalid OTP)"
            }
            throwable is com.google.firebase.FirebaseTooManyRequestsException ||
                    message.contains("TOO_LONG_WAIT", ignoreCase = true) ||
                    message.contains("too-many-requests", ignoreCase = true) ||
                    message.contains("blocked all requests", ignoreCase = true) -> {
                "अत्यधिक प्रयास। कुछ देर बाद प्रयास करें"
            }
            throwable is com.google.firebase.FirebaseNetworkException ||
                    message.contains("network", ignoreCase = true) -> {
                "इंटरनेट समस्या (Network Error)"
            }
            throwable is kotlinx.coroutines.TimeoutCancellationException ||
                    message.contains("timed out", ignoreCase = true) -> {
                "अनुरोध समय समाप्त (Timed out)"
            }
            message.contains("appNotAuthorized", ignoreCase = true) ||
                    message.contains("17010", ignoreCase = true) ||
                    message.contains("17028", ignoreCase = true) -> {
                "Firebase अधिकृत नहीं है (SHA Fingerprint)"
            }
            message.contains("missing-client-identifier", ignoreCase = true) ||
                    message.contains("reCAPTCHA", ignoreCase = true) -> {
                "डिवाइस सत्यापन विफल (Google Play Services)"
            }
            else -> {
                message.take(60)
            }
        }
    }

    fun validateMobile(): Boolean {
        val cleanPhone = mobileNumber.trim()
        return if (cleanPhone.length != 10 || !cleanPhone.all { it.isDigit() }) {
            mobileError = "10 अंकों का वैध मोबाइल नंबर दर्ज करें"
            false
        } else {
            mobileError = null
            true
        }
    }

    fun resolveUserProfileAndProceed(uid: String) {
        isLoading = true
        generalError = null
        coroutineScope.launch {
            try {
                val userResult = ShramSarthiRepositoryProvider.userRepository.getUser(uid)
                isLoading = false
                userResult.onSuccess { profile ->
                    if (profile != null && profile.isProfileComplete()) {
                        successNotice = "सत्यापित! प्रवेश हो रहा है..."
                        delay(350)
                        onLoginSuccessWithRole(profile.role, false)
                    } else {
                        // User is authenticated in Firebase Auth, but profile setup is incomplete
                        successNotice = "नंबर सत्यापित! प्रोफ़ाइल पूर्ण करें..."
                        delay(350)
                        onLoginSuccessWithRole(profile?.role, true)
                    }
                }.onFailure { exc ->
                    generalError = exc.localizedMessage ?: "प्रोफ़ाइल लोड विफल। कृपया पुनः प्रयास करें।"
                }
            } catch (e: Exception) {
                isLoading = false
                generalError = e.localizedMessage ?: "त्रुटि हुई। कृपया पुनः प्रयास करें।"
            }
        }
    }

    fun handleSendOtp() {
        if (isLoading) return
        if (!validateMobile()) return
        if (activity == null) {
            generalError = "Activity context is unavailable"
            return
        }

        isLoading = true
        generalError = null
        successNotice = null

        try {
            ShramSarthiRepositoryProvider.authRepository.sendOtp(
                activity = activity,
                phoneNumber = mobileNumber.trim(),
                onCodeSent = { vId ->
                    isLoading = false
                    verificationId = vId
                    isOtpSent = true
                    resendTimer = 60
                    successNotice = "ओटीपी भेजा गया (+91 $mobileNumber)"
                },
                onAutoVerified = { uid ->
                    // Auto-verification succeeded via Firebase Phone Auth / Google Play Services
                    isOtpSent = true
                    otpCode = "••••••"
                    isLoading = true
                    successNotice = "स्वतः सत्यापित! प्रोफ़ाइल जांची जा रही है..."
                    generalError = null
                    coroutineScope.launch {
                        delay(800)
                        val authRepo = ShramSarthiRepositoryProvider.authRepository
                        if (authRepo.isUserLoggedIn() && authRepo.getCurrentUserId() == uid) {
                            resolveUserProfileAndProceed(uid)
                        } else {
                            isLoading = false
                            generalError = "कृपया ओटीपी मैन्युअल रूप से दर्ज करें"
                            isOtpSent = true
                        }
                    }
                },
                onTimeout = { vId ->
                    isLoading = false
                    if (verificationId == null) {
                        verificationId = vId
                    }
                    isOtpSent = true
                    generalError = "कृपया प्राप्त 6-अंकीय ओटीपी दर्ज करें"
                },
                onError = { exc ->
                    isLoading = false
                    generalError = formatFirebaseError(exc)
                }
            )
        } catch (e: Exception) {
            isLoading = false
            generalError = formatFirebaseError(e)
        }
    }

    fun handleVerifyOtp() {
        if (isLoading) return
        val cleanOtp = otpCode.trim()
        if (cleanOtp.length != 6 || !cleanOtp.all { it.isDigit() }) {
            otpError = "6 अंकों का ओटीपी दर्ज करें"
            return
        }
        val vId = verificationId
        if (vId == null) {
            generalError = "कृपया पहले ओटीपी प्राप्त करें"
            return
        }

        isLoading = true
        generalError = null
        otpError = null

        coroutineScope.launch {
            try {
                val result = withTimeoutOrNull(15000L) {
                    ShramSarthiRepositoryProvider.authRepository.signInWithOtp(vId, cleanOtp)
                }

                if (result == null) {
                    isLoading = false
                    generalError = "समय समाप्त। कृपया पुनः प्रयास करें"
                    return@launch
                }

                result.onSuccess { uid ->
                    val authRepo = ShramSarthiRepositoryProvider.authRepository
                    if (authRepo.isUserLoggedIn() && authRepo.getCurrentUserId() == uid) {
                        successNotice = "ओटीपी सत्यापित! प्रोफ़ाइल जांची जा रही है..."
                        resolveUserProfileAndProceed(uid)
                    } else {
                        generalError = "सत्र त्रुटि। पुनः प्रयास करें"
                        isLoading = false
                    }
                }.onFailure { exc ->
                    isLoading = false
                    generalError = formatFirebaseError(exc)
                }
            } catch (e: Exception) {
                isLoading = false
                generalError = formatFirebaseError(e)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP SECTION: Optional Back button + Logo + Branding
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, BorderSubtle, CircleShape)
                    ) {
                        Icon(
                            imageVector = ShramIcons.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Compact Logo & Title
                BrandLogoBadge(size = 48)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isHindi) "श्रम सारथी" else "Shram Sarthi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = if (isHindi) "श्रम सारथी • प्रवेश द्वार" else "Shram Sarthi • Login Portal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CENTER SECTION: Card with Phone or OTP
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!isOtpSent) {
                            // --- PHONE INPUT STATE ---
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isHindi) "मोबाइल नंबर दर्ज करें" else "Enter Mobile Number",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Phone Field with +91 Prefix
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        if (mobileError != null) Color(0xFFDC2626) else BorderStrong,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(TealContainer, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "🇮🇳 +91",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TealPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedTextField(
                                    value = mobileNumber,
                                    onValueChange = {
                                        if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                            mobileNumber = it
                                            mobileError = null
                                            generalError = null
                                        }
                                    },
                                    placeholder = {
                                        Text(
                                            text = if (isHindi) "10 अंकों का नंबर" else "10-digit number",
                                            fontSize = 14.sp,
                                            color = TextMuted
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    )
                                )
                            }

                            if (mobileError != null) {
                                Text(
                                    text = mobileError ?: "",
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp, start = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            ShramPrimaryButton(
                                text = if (isHindi) "ओटीपी प्राप्त करें" else "Get OTP",
                                onClick = { handleSendOtp() },
                                isLoading = isLoading,
                                modifier = Modifier.height(48.dp)
                            )
                        } else {
                            // --- OTP INPUT STATE ---
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isHindi) "ओटीपी दर्ज करें" else "Enter OTP",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isHindi) "+91 $mobileNumber पर भेजा गया" else "Sent to +91 $mobileNumber",
                                        fontSize = 11.sp,
                                        color = TealPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .clickable {
                                            isOtpSent = false
                                            otpCode = ""
                                            generalError = null
                                            otpError = null
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isHindi) "बदलें" else "Edit",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = {
                                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                        otpCode = it
                                        otpError = null
                                        generalError = null
                                    }
                                },
                                placeholder = {
                                    Text(
                                        text = "• • • • • •",
                                        fontSize = 18.sp,
                                        color = TextMuted,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = 6.sp,
                                    color = TextPrimary
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TealPrimary,
                                    unfocusedBorderColor = BorderStrong,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color(0xFFF8FAFC)
                                )
                            )

                            if (otpError != null) {
                                Text(
                                    text = otpError ?: "",
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp, start = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Resend Timer Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (resendTimer > 0) "पुनः भेजें: ${resendTimer}s" else "ओटीपी नहीं मिला?",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )

                                if (resendTimer == 0) {
                                    Text(
                                        text = "ओटीपी पुनः भेजें",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronAccent,
                                        modifier = Modifier.clickable { handleSendOtp() }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            ShramPrimaryButton(
                                text = if (isHindi) "सत्यापित करें" else "Verify OTP",
                                onClick = { handleVerifyOtp() },
                                isLoading = isLoading,
                                modifier = Modifier.height(48.dp)
                            )
                        }
                    }
                }

                // Short Inline Error Banner
                AnimatedVisibility(visible = generalError != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(Color(0xFFFEF2F2), RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ShramIcons.Info,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = generalError ?: "",
                            fontSize = 11.sp,
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        if (ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                            val authUid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId()
                            if (authUid != null) {
                                Text(
                                    text = "पुनः प्रयास",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626),
                                    modifier = Modifier
                                        .clickable { resolveUserProfileAndProceed(authUid) }
                                        .padding(start = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Short Inline Success Banner
                AnimatedVisibility(visible = successNotice != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(Color(0xFFECFDF5), RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ShramIcons.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = successNotice ?: "",
                            fontSize = 11.sp,
                            color = Color(0xFF047857),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // BOTTOM SECTION: Small registration link
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isHindi) "खाता नहीं है? " else "Don't have an account? ",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = if (isHindi) "पंजीकरण करें" else "Register",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}
