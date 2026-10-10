package com.example.shramsarthi.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.shramsarthi.data.ShramSarthiRepositoryProvider
import com.example.shramsarthi.model.User
import com.example.shramsarthi.model.UserRole
import com.example.shramsarthi.navigation.Screen
import com.example.shramsarthi.navigation.rememberNavigationState
import com.example.shramsarthi.ui.screens.BuilderDashboardScreen
import com.example.shramsarthi.ui.screens.CustomerDashboardScreen
import com.example.shramsarthi.ui.screens.LanguageSelectionScreen
import com.example.shramsarthi.ui.screens.LoginScreen
import com.example.shramsarthi.ui.screens.RegisterScreen
import com.example.shramsarthi.ui.screens.RoleSelectionScreen
import com.example.shramsarthi.ui.screens.TermsAndConditionsScreen
import com.example.shramsarthi.ui.screens.ThekedarDashboardScreen
import com.example.shramsarthi.ui.screens.WelcomeScreen
import com.example.shramsarthi.ui.screens.AiSaathiScreen
import com.example.shramsarthi.ui.screens.WorkerDashboardScreen
import com.example.shramsarthi.ui.theme.BackgroundLight
import com.example.shramsarthi.util.LanguageManager
import com.example.shramsarthi.util.UserSessionManager
import kotlinx.coroutines.launch

/**
 * Top-level Application container for Shram Sarthi.
 * Enforces real Firebase session verification, handles seamless role routing,
 * and guards dashboards against unauthenticated access.
 */
@Composable
fun ShramSarthiApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val hasSelectedLanguage = remember { LanguageManager.hasUserSelectedLanguage(context) }
    val initialScreen = if (hasSelectedLanguage) Screen.Welcome else Screen.LanguageSelection
    val navState = rememberNavigationState(initialScreen = initialScreen)
    val coroutineScope = rememberCoroutineScope()
    val cachedRole = remember { UserSessionManager.getUserRole(context) }
    var activeRole by remember { mutableStateOf(cachedRole) }

    // Resume session if Firebase user is already logged in
    var isStartupHandled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!isStartupHandled) {
            isStartupHandled = true
            val authRepo = ShramSarthiRepositoryProvider.authRepository
            if (authRepo.isUserLoggedIn()) {
                val uid = authRepo.getCurrentUserId()
                if (uid != null) {
                    UserSessionManager.saveCachedUid(context, uid)
                    val profileResult = ShramSarthiRepositoryProvider.userRepository.getUser(uid)
                    profileResult.onSuccess { profile ->
                        if (profile != null && profile.isProfileComplete()) {
                            // If user previously selected a role (e.g. Worker) locally, respect that selection
                            val resolvedRole = cachedRole ?: profile.role
                            activeRole = resolvedRole
                            UserSessionManager.saveUserRole(context, resolvedRole)
                            if (resolvedRole != profile.role) {
                                coroutineScope.launch {
                                    val defaultSkill = if (resolvedRole == UserRole.WORKER) "राजमिस्त्री (Mason)" else null
                                    ShramSarthiRepositoryProvider.userRepository.updateRole(uid, resolvedRole, defaultSkill)
                                }
                            }
                            if (navState.currentScreen is Screen.Welcome) {
                                when (resolvedRole) {
                                    UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                    UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                    UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                    UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                                }
                            }
                        } else if (profile != null && !profile.isProfileComplete()) {
                            if (navState.currentScreen is Screen.Welcome) {
                                navState.navigateTo(Screen.Register)
                            }
                        }
                    }.onFailure {
                        // Fall back to locally cached role on network drop or timeout
                        val fallback = cachedRole ?: UserSessionManager.getUserRole(context)
                        if (fallback != null && navState.currentScreen is Screen.Welcome) {
                            activeRole = fallback
                            when (fallback) {
                                UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                            }
                        }
                    }
                }
            }
        }
    }

    // Hardware back navigation
    BackHandler(enabled = navState.canNavigateBack) {
        navState.navigateBack()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundLight
    ) {
        Crossfade(
            targetState = navState.currentScreen,
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                is Screen.LanguageSelection -> {
                    LanguageSelectionScreen(
                        onLanguageConfirmed = {
                            coroutineScope.launch {
                                val authRepo = ShramSarthiRepositoryProvider.authRepository
                                if (authRepo.isUserLoggedIn()) {
                                    val uid = authRepo.getCurrentUserId()
                                    var resolvedRole = activeRole ?: UserSessionManager.getUserRole(context)
                                    if (resolvedRole == null && uid != null) {
                                        val profile = ShramSarthiRepositoryProvider.userRepository.getUser(uid).getOrNull()
                                        if (profile != null && profile.isProfileComplete()) {
                                            resolvedRole = profile.role
                                            activeRole = profile.role
                                            UserSessionManager.saveUserRole(context, profile.role)
                                        }
                                    }
                                    if (resolvedRole != null) {
                                        when (resolvedRole) {
                                            UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                            UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                            UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                            UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                                        }
                                    } else {
                                        navState.clearAndNavigate(Screen.Welcome)
                                    }
                                } else {
                                    navState.clearAndNavigate(Screen.Welcome)
                                }
                            }
                        }
                    )
                }

                is Screen.Welcome -> {
                    WelcomeScreen(
                        onGetStartedClick = {
                            if (ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                                val resolvedRole = activeRole ?: UserSessionManager.getUserRole(context)
                                if (resolvedRole != null) {
                                    when (resolvedRole) {
                                        UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                        UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                        UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                        UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                                    }
                                } else {
                                    coroutineScope.launch {
                                        val uid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId()
                                        if (uid != null) {
                                            val profile = ShramSarthiRepositoryProvider.userRepository.getUser(uid).getOrNull()
                                            if (profile != null && profile.isProfileComplete()) {
                                                activeRole = profile.role
                                                UserSessionManager.saveUserRole(context, profile.role)
                                                when (profile.role) {
                                                    UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                                    UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                                    UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                                    UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                                                }
                                            } else {
                                                navState.navigateTo(Screen.Register)
                                            }
                                        } else {
                                            navState.navigateTo(Screen.Login)
                                        }
                                    }
                                }
                            } else {
                                navState.navigateTo(Screen.Login)
                            }
                        },
                        onLoginClick = {
                            navState.navigateTo(Screen.Login)
                        },
                        onExploreRolesClick = {
                            navState.navigateTo(Screen.RoleSelection)
                        }
                    )
                }

                is Screen.Login -> {
                    LoginScreen(
                        onNavigateBack = {
                            navState.navigateBack()
                        },
                        onLoginSuccessWithRole = { userRole, isNewUser ->
                            if (isNewUser || userRole == null) {
                                navState.navigateTo(Screen.Register)
                            } else {
                                activeRole = userRole
                                UserSessionManager.saveUserRole(context, userRole)
                                when (userRole) {
                                    UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                    UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                    UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                    UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                                }
                            }
                        },
                        onNavigateToRegister = {
                            navState.navigateTo(Screen.Register)
                        }
                    )
                }

                is Screen.Register -> {
                    RegisterScreen(
                        onNavigateBack = {
                            navState.navigateBack()
                        },
                        onRegisterSuccess = { selectedRole ->
                            activeRole = selectedRole
                            UserSessionManager.saveUserRole(context, selectedRole)
                            when (selectedRole) {
                                UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                            }
                        },
                        onNavigateToLogin = {
                            navState.navigateTo(Screen.Login)
                        },
                        onNavigateToTerms = {
                            navState.navigateTo(Screen.TermsAndConditions)
                        }
                    )
                }

                is Screen.TermsAndConditions -> {
                    TermsAndConditionsScreen(
                        onNavigateBack = {
                            navState.navigateBack()
                        }
                    )
                }

                is Screen.RoleSelection -> {
                    RoleSelectionScreen(
                        onNavigateBack = {
                            navState.navigateBack()
                        },
                        initialRole = activeRole ?: UserSessionManager.getUserRole(context) ?: UserRole.WORKER,
                        onRoleSelected = { role ->
                            activeRole = role
                            UserSessionManager.saveUserRole(context, role)
                            if (!ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                                // Strictly protect dashboards: unauthenticated users must sign in via OTP
                                navState.navigateTo(Screen.Login)
                            } else {
                                // Instantly navigate to target dashboard without delay
                                when (role) {
                                    UserRole.WORKER -> navState.clearAndNavigate(Screen.WorkerDashboard)
                                    UserRole.THEKEDAR -> navState.clearAndNavigate(Screen.ThekedarDashboard)
                                    UserRole.BUILDER -> navState.clearAndNavigate(Screen.BuilderDashboard)
                                    UserRole.CUSTOMER -> navState.clearAndNavigate(Screen.CustomerDashboard)
                                }

                                // Persist updated role directly to Firestore asynchronously
                                coroutineScope.launch {
                                    try {
                                        val uid = ShramSarthiRepositoryProvider.authRepository.getCurrentUserId()
                                        if (uid != null) {
                                            val defaultSkillForRole = when (role) {
                                                UserRole.WORKER -> "राजमिस्त्री (Mason)"
                                                UserRole.THEKEDAR -> "सिविल ठेकेदार (Civil Contractor)"
                                                UserRole.BUILDER -> "आवासीय निर्माण (Residential Projects)"
                                                UserRole.CUSTOMER -> "घरेलू मरम्मत (Home Maintenance)"
                                            }
                                            ShramSarthiRepositoryProvider.userRepository.updateRole(uid, role, defaultSkillForRole)
                                        }
                                    } catch (e: Exception) {
                                        android.util.Log.e("ShramSarthiApp", "Role update error: ${e.message}")
                                    }
                                }
                            }
                        }
                    )
                }

                is Screen.WorkerDashboard -> {
                    if (!ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                        navState.clearAndNavigate(Screen.Login)
                    } else {
                        WorkerDashboardScreen(
                            onNavigateBack = {
                                navState.navigateBack()
                            },
                            onLogoutClick = {
                                coroutineScope.launch {
                                    ShramSarthiRepositoryProvider.authRepository.signOut()
                                    UserSessionManager.clearSession(context)
                                    activeRole = null
                                }
                                navState.clearAndNavigate(Screen.Welcome)
                            },
                            onSwitchRoleClick = {
                                navState.navigateTo(Screen.RoleSelection)
                            },
                            onOpenTermsClick = {
                                navState.navigateTo(Screen.TermsAndConditions)
                            },
                            onOpenAiSaathiClick = {
                                navState.navigateTo(Screen.AiSaathi)
                            }
                        )
                    }
                }

                is Screen.ThekedarDashboard -> {
                    if (!ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                        navState.clearAndNavigate(Screen.Login)
                    } else {
                        ThekedarDashboardScreen(
                            onNavigateBack = {
                                navState.navigateBack()
                            },
                            onLogoutClick = {
                                coroutineScope.launch {
                                    ShramSarthiRepositoryProvider.authRepository.signOut()
                                    UserSessionManager.clearSession(context)
                                    activeRole = null
                                }
                                navState.clearAndNavigate(Screen.Welcome)
                            },
                            onSwitchRoleClick = {
                                navState.navigateTo(Screen.RoleSelection)
                            },
                            onOpenTermsClick = {
                                navState.navigateTo(Screen.TermsAndConditions)
                            }
                        )
                    }
                }

                is Screen.BuilderDashboard -> {
                    if (!ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                        navState.clearAndNavigate(Screen.Login)
                    } else {
                        BuilderDashboardScreen(
                            onNavigateBack = {
                                navState.navigateBack()
                            },
                            onLogoutClick = {
                                coroutineScope.launch {
                                    ShramSarthiRepositoryProvider.authRepository.signOut()
                                    UserSessionManager.clearSession(context)
                                    activeRole = null
                                }
                                navState.clearAndNavigate(Screen.Welcome)
                            },
                            onSwitchRoleClick = {
                                navState.navigateTo(Screen.RoleSelection)
                            },
                            onOpenTermsClick = {
                                navState.navigateTo(Screen.TermsAndConditions)
                            }
                        )
                    }
                }

                is Screen.CustomerDashboard -> {
                    if (!ShramSarthiRepositoryProvider.authRepository.isUserLoggedIn()) {
                        navState.clearAndNavigate(Screen.Login)
                    } else {
                        CustomerDashboardScreen(
                            onNavigateBack = {
                                navState.navigateBack()
                            },
                            onLogoutClick = {
                                coroutineScope.launch {
                                    ShramSarthiRepositoryProvider.authRepository.signOut()
                                    UserSessionManager.clearSession(context)
                                    activeRole = null
                                }
                                navState.clearAndNavigate(Screen.Welcome)
                            },
                            onSwitchRoleClick = {
                                navState.navigateTo(Screen.RoleSelection)
                            },
                            onOpenTermsClick = {
                                navState.navigateTo(Screen.TermsAndConditions)
                            }
                        )
                    }
                }

                is Screen.AiSaathi -> {
                    AiSaathiScreen(
                        onNavigateBack = {
                            navState.navigateBack()
                        }
                    )
                }
            }
        }
    }
}
