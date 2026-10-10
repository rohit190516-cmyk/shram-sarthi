package com.example.shramsarthi.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.User
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.model.getLocalizedSkill
import com.example.shramsarthi.ui.components.BrandLogoBadge
import com.example.shramsarthi.ui.components.ShramIcons
import com.example.shramsarthi.ui.components.ShramPrimaryButton
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.ui.theme.BorderStrong
import com.example.shramsarthi.ui.theme.BorderSubtle
import com.example.shramsarthi.ui.theme.TealContainer
import com.example.shramsarthi.ui.theme.TealPrimary
import com.example.shramsarthi.ui.theme.TextMuted
import com.example.shramsarthi.ui.theme.TextPrimary
import com.example.shramsarthi.ui.theme.TextSecondary
import com.example.shramsarthi.util.AppLanguage
import com.example.shramsarthi.util.LocalAppLanguage
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: (UserRole) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit = {}
) {
    val language = LocalAppLanguage.current
    val isHindi = language == AppLanguage.HINDI

    val authRepo = ShramSarthiRepositoryProvider.authRepository
    val userRepo = ShramSarthiRepositoryProvider.userRepository
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isUserLoggedIn = authRepo.isUserLoggedIn()
    val authUid = authRepo.getCurrentUserId() ?: ""
    val verifiedPhone = authRepo.getCurrentUserPhone() ?: ""

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.WORKER) }
    var selectedSkill by remember { mutableStateOf(selectedRole.getLocalizedSkills(isHindi).firstOrNull() ?: "") }
    var termsAccepted by remember { mutableStateOf(false) }

    var existingCreatedAt by remember { mutableStateOf<Long?>(null) }
    var isInitialDataLoaded by remember { mutableStateOf(false) }

    // Validation & State
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }
    var skillError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Pre-populate if partial existing profile exists in Firestore
    LaunchedEffect(authUid) {
        if (authUid.isNotBlank() && !isInitialDataLoaded) {
            val existing = userRepo.getUser(authUid).getOrNull()
            if (existing != null) {
                if (existing.fullName.isNotBlank()) fullName = existing.fullName
                if (existing.location.isNotBlank()) location = existing.location
                selectedRole = existing.role
                if (existing.primarySkill.isNotBlank()) {
                    selectedSkill = getLocalizedSkill(existing.primarySkill, isHindi)
                }
                termsAccepted = existing.termsAccepted
                existingCreatedAt = existing.createdAt
            }
            isInitialDataLoaded = true
        }
    }

    // Keep selectedSkill synced when language or role changes
    LaunchedEffect(selectedRole, isHindi) {
        val currentSkills = selectedRole.getLocalizedSkills(isHindi)
        if (selectedSkill.isBlank() || !currentSkills.contains(selectedSkill)) {
            selectedSkill = currentSkills.firstOrNull() ?: ""
        }
    }

    fun onRoleChange(role: UserRole) {
        selectedRole = role
        selectedSkill = role.getLocalizedSkills(isHindi).firstOrNull() ?: ""
        skillError = null
    }

    val isFormValid: Boolean = remember(fullName, location, selectedRole, selectedSkill, termsAccepted) {
        fullName.trim().length >= 2 &&
                location.trim().isNotBlank() &&
                termsAccepted &&
                (selectedRole != UserRole.WORKER || selectedSkill.trim().isNotBlank())
    }

    fun handleRegisterSubmit() {
        if (isLoading) return

        var hasError = false
        val cleanName = fullName.trim()
        if (cleanName.length < 2) {
            fullNameError = if (isHindi) "कृपया पूरा नाम दर्ज करें (कम से कम 2 अक्षर)" else "Please enter full name (at least 2 letters)"
            hasError = true
        } else {
            fullNameError = null
        }

        val cleanLoc = location.trim()
        if (cleanLoc.isBlank()) {
            locationError = if (isHindi) "शहर या क्षेत्र दर्ज करें" else "Please enter city or area"
            hasError = true
        } else {
            locationError = null
        }

        if (selectedRole == UserRole.WORKER && selectedSkill.trim().isBlank()) {
            skillError = if (isHindi) "मुख्य कौशल चुनें" else "Please select primary skill"
            hasError = true
        } else {
            skillError = null
        }

        if (!termsAccepted) {
            termsError = if (isHindi) "पंजीकरण के लिए नियम व शर्तें स्वीकार करना आवश्यक है" else "You must agree to the Terms & Privacy Policy"
            hasError = true
        } else {
            termsError = null
        }

        if (hasError) {
            generalError = if (isHindi) "कृपया आवश्यक विवरण भरें और शर्तें स्वीकार करें" else "Please fill all required fields and accept terms"
            return
        }

        val currentFirebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val effectiveUid = currentFirebaseUser?.uid ?: authUid
        if (effectiveUid.isBlank()) {
            generalError = if (isHindi) "सत्र अमान्य: कृपया पहले मोबाइल ओटीपी से लॉगिन करें" else "Session expired: Please login via phone OTP first"
            return
        }

        isLoading = true
        generalError = null

        coroutineScope.launch {
            try {
                val user = User(
                    uid = effectiveUid,
                    fullName = cleanName,
                    mobileNumber = verifiedPhone.ifBlank { currentFirebaseUser?.phoneNumber ?: "" },
                    location = cleanLoc,
                    role = selectedRole,
                    primarySkill = selectedSkill.trim(),
                    createdAt = existingCreatedAt ?: System.currentTimeMillis(),
                    termsAccepted = true,
                    termsAcceptedAt = System.currentTimeMillis()
                )

                android.util.Log.d("RegisterScreen", "Submitting registration for uid=$effectiveUid, role=${selectedRole.name}")
                val result = withTimeout(15000L) {
                    userRepo.saveUser(user)
                }

                result.onSuccess {
                    android.util.Log.i("RegisterScreen", "saveUser succeeded. Navigating to dashboard for role=${selectedRole.name}")
                    isLoading = false
                    onRegisterSuccess(selectedRole)
                }.onFailure { exc ->
                    android.util.Log.w("RegisterScreen", "saveUser reported failure: ${exc.message}")

                    // Check if profile was saved in Firestore despite error
                    val checkResult = userRepo.getUser(effectiveUid)
                    val verifiedProfile = checkResult.getOrNull()
                    if (verifiedProfile != null && verifiedProfile.isProfileComplete()) {
                        android.util.Log.i("RegisterScreen", "Profile verified complete in Firestore. Proceeding.")
                        isLoading = false
                        onRegisterSuccess(verifiedProfile.role)
                        return@launch
                    }

                    isLoading = false
                    generalError = exc.localizedMessage ?: if (isHindi) "प्रोफ़ाइल सहेजने में विफल। कृपया पुनः प्रयास करें।" else "Failed to save profile. Please retry."
                }
            } catch (e: Exception) {
                android.util.Log.e("RegisterScreen", "Registration exception: ${e.message}", e)
                val checkResult = userRepo.getUser(effectiveUid)
                val verifiedProfile = checkResult.getOrNull()
                if (verifiedProfile != null && verifiedProfile.isProfileComplete()) {
                    android.util.Log.i("RegisterScreen", "Profile verified after exception. Proceeding.")
                    isLoading = false
                    onRegisterSuccess(verifiedProfile.role)
                    return@launch
                }
                isLoading = false
                generalError = if (isHindi) "नेटवर्क समय समाप्त या त्रुटि। कृपया पुनः प्रयास करें।" else "Network timeout or error. Please retry."
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
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                        contentDescription = if (isHindi) "पीछे जाएं" else "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandLogoBadge(size = 32)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) "श्रम सारथी" else "Shram Sarthi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // TITLE
            Text(
                text = if (isHindi) "प्रोफ़ाइल पंजीकरण" else "Create Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isHindi) "श्रम नेटवर्क में जुड़ने के लिए विवरण पूरा करें" else "Complete your details to join the network",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isUserLoggedIn) {
                // Phone OTP requirement card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = ShramIcons.Phone,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isHindi) "सत्यापित मोबाइल नंबर आवश्यक है" else "Verified Mobile Number Required",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) "प्रोफ़ाइल बनाने से पहले मोबाइल नंबर का ओटीपी सत्यापन आवश्यक है।" else "Phone OTP verification is required before creating a profile.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ShramPrimaryButton(
                            text = if (isHindi) "ओटीपी से लॉगिन करें" else "Verify Phone OTP",
                            onClick = onNavigateToLogin,
                            modifier = Modifier.height(46.dp)
                        )
                    }
                }
            } else {
                // AUTHENTICATED USER REGISTRATION FORM
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        // 1. VERIFIED PHONE NUMBER (READ-ONLY)
                        Text(
                            text = if (isHindi) "सत्यापित मोबाइल नंबर *" else "Verified Mobile Number *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                                .border(1.dp, BorderStrong, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ShramIcons.Phone,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = verifiedPhone.ifBlank { if (isHindi) "मोबाइल नंबर सत्यापित" else "Phone Verified" },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .background(Color(0xFFECFDF5), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ShramIcons.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isHindi) "सत्यापित" else "Verified",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 2. FULL NAME
                        Text(
                            text = if (isHindi) "पूरा नाम *" else "Full Name *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = {
                                fullName = it
                                fullNameError = null
                                generalError = null
                            },
                            placeholder = {
                                Text(
                                    text = if (isHindi) "उदा. रमेश कुमार" else "e.g. Amit Sharma",
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = fullNameError != null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealPrimary,
                                unfocusedBorderColor = BorderStrong,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            )
                        )

                        if (fullNameError != null) {
                            Text(
                                text = fullNameError ?: "",
                                color = Color(0xFFDC2626),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3. ROLE SELECTION
                        Text(
                            text = if (isHindi) "आपकी भूमिका *" else "Select Role *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RoleCardItem(
                                role = UserRole.WORKER,
                                isHindi = isHindi,
                                isSelected = selectedRole == UserRole.WORKER,
                                onClick = { onRoleChange(UserRole.WORKER) },
                                modifier = Modifier.weight(1f)
                            )
                            RoleCardItem(
                                role = UserRole.THEKEDAR,
                                isHindi = isHindi,
                                isSelected = selectedRole == UserRole.THEKEDAR,
                                onClick = { onRoleChange(UserRole.THEKEDAR) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RoleCardItem(
                                role = UserRole.BUILDER,
                                isHindi = isHindi,
                                isSelected = selectedRole == UserRole.BUILDER,
                                onClick = { onRoleChange(UserRole.BUILDER) },
                                modifier = Modifier.weight(1f)
                            )
                            RoleCardItem(
                                role = UserRole.CUSTOMER,
                                isHindi = isHindi,
                                isSelected = selectedRole == UserRole.CUSTOMER,
                                onClick = { onRoleChange(UserRole.CUSTOMER) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4. PRIMARY SKILL / TRADE FOCUS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedRole == UserRole.WORKER) {
                                    if (isHindi) "मुख्य कौशल *" else "Primary Skill *"
                                } else {
                                    if (isHindi) "कार्य क्षेत्र" else "Trade Focus"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                            if (selectedRole != UserRole.WORKER) {
                                Text(
                                    text = if (isHindi) "वैकल्पिक" else "Optional",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedRole.getLocalizedSkills(isHindi).forEach { skill ->
                                val isSelected = selectedSkill == skill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isSelected) TealPrimary else Color.White)
                                        .border(
                                            1.dp,
                                            if (isSelected) TealPrimary else BorderStrong,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            selectedSkill = skill
                                            skillError = null
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = skill,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                }
                            }
                        }

                        if (skillError != null) {
                            Text(
                                text = skillError ?: "",
                                color = Color(0xFFDC2626),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 5. LOCATION / CITY
                        Text(
                            text = if (isHindi) "शहर या क्षेत्र *" else "City or Area *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = location,
                            onValueChange = {
                                location = it
                                locationError = null
                                generalError = null
                            },
                            placeholder = {
                                Text(
                                    text = if (isHindi) "उदा. सीतापुरा, जयपुर" else "e.g. Sitapura, Jaipur",
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = locationError != null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealPrimary,
                                unfocusedBorderColor = BorderStrong,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            )
                        )

                        if (locationError != null) {
                            Text(
                                text = locationError ?: "",
                                color = Color(0xFFDC2626),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 6. TERMS & CONDITIONS CHECKBOX ROW (Smooth natural wrapping beside checkbox)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(
                                    1.dp,
                                    if (termsError != null) Color(0xFFDC2626) else BorderSubtle,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Checkbox(
                                checked = termsAccepted,
                                onCheckedChange = {
                                    termsAccepted = it
                                    termsError = null
                                    generalError = null
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = TealPrimary,
                                    checkmarkColor = Color.White
                                ),
                                modifier = Modifier.padding(top = 0.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            val termsAnnotated = remember(isHindi) {
                                buildAnnotatedString {
                                    if (isHindi) {
                                        append("मैं श्रम सारथी के ")
                                        pushStringAnnotation(tag = "TERMS", annotation = "terms")
                                        withStyle(SpanStyle(color = TealPrimary, fontWeight = FontWeight.Bold)) {
                                            append("नियम व शर्तें")
                                        }
                                        pop()
                                        append(" और ")
                                        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                                        withStyle(SpanStyle(color = TealPrimary, fontWeight = FontWeight.Bold)) {
                                            append("गोपनीयता नीति")
                                        }
                                        pop()
                                        append(" को स्वीकार करता/करती हूँ *")
                                    } else {
                                        append("I agree to Shram Sarthi's ")
                                        pushStringAnnotation(tag = "TERMS", annotation = "terms")
                                        withStyle(SpanStyle(color = TealPrimary, fontWeight = FontWeight.Bold)) {
                                            append("Terms & Conditions")
                                        }
                                        pop()
                                        append(" and ")
                                        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                                        withStyle(SpanStyle(color = TealPrimary, fontWeight = FontWeight.Bold)) {
                                            append("Privacy Policy")
                                        }
                                        pop()
                                        append(" *")
                                    }
                                }
                            }

                            ClickableText(
                                text = termsAnnotated,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    lineHeight = 18.sp
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp),
                                onClick = { offset ->
                                    val annotation = termsAnnotated.getStringAnnotations(offset, offset).firstOrNull()
                                    if (annotation != null && (annotation.tag == "TERMS" || annotation.tag == "PRIVACY")) {
                                        onNavigateToTerms()
                                    } else {
                                        termsAccepted = !termsAccepted
                                        termsError = null
                                        generalError = null
                                    }
                                }
                            )
                        }

                        if (termsError != null) {
                            Text(
                                text = termsError ?: "",
                                color = Color(0xFFDC2626),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // CREATE ACCOUNT SUBMIT BUTTON
                        ShramPrimaryButton(
                            text = if (isHindi) {
                                if (existingCreatedAt != null) "प्रोफ़ाइल अपडेट करें" else "खाता बनाएं"
                            } else {
                                if (existingCreatedAt != null) "Update Profile" else "Create Account"
                            },
                            onClick = { handleRegisterSubmit() },
                            isLoading = isLoading,
                            loadingText = if (isHindi) "सहेजा जा रहा है..." else "Saving...",
                            enabled = isFormValid && !isLoading,
                            modifier = Modifier.height(48.dp)
                        )
                    }
                }

                // Error Banner with Retry
                AnimatedVisibility(visible = generalError != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
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
                        Text(
                            text = if (isHindi) "पुनः प्रयास" else "Retry",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626),
                            modifier = Modifier
                                .clickable { handleRegisterSubmit() }
                                .padding(start = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Link to Full Terms
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "📄 पूर्ण नियम व शर्तें पढ़ें" else "📄 Read Full Terms & Conditions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TealPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onNavigateToTerms() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun RoleCardItem(
    role: UserRole,
    isHindi: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) role.badgeColor.copy(alpha = 0.12f) else Color.White)
            .border(
                if (isSelected) 1.5.dp else 1.dp,
                if (isSelected) role.badgeColor else BorderSubtle,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(role.badgeColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = role.getLocalizedTitle(isHindi),
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) role.badgeColor else TextPrimary
            )
        }
    }
}
