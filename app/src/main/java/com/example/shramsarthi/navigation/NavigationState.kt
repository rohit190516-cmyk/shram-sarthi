package com.example.shramsarthi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

sealed interface Screen {
    data object LanguageSelection : Screen
    data object Welcome : Screen
    data object Login : Screen
    data object Register : Screen
    data object RoleSelection : Screen
    data object WorkerDashboard : Screen
    data object ThekedarDashboard : Screen
    data object BuilderDashboard : Screen
    data object CustomerDashboard : Screen
    data object TermsAndConditions : Screen
    data object AiSaathi : Screen
}

class NavigationState(initialScreen: Screen = Screen.Welcome) {
    val backStack = mutableStateListOf<Screen>(initialScreen)

    val currentScreen: Screen
        get() = backStack.lastOrNull() ?: Screen.Welcome

    val canNavigateBack: Boolean
        get() = backStack.size > 1

    fun navigateTo(screen: Screen) {
        if (backStack.lastOrNull() != screen) {
            backStack.add(screen)
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            return true
        }
        return false
    }

    fun replaceWith(screen: Screen) {
        if (backStack.isNotEmpty()) {
            backStack.removeAt(backStack.lastIndex)
        }
        backStack.add(screen)
    }

    fun clearAndNavigate(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
    }
}

@Composable
fun rememberNavigationState(initialScreen: Screen = Screen.Welcome): NavigationState {
    return remember { NavigationState(initialScreen) }
}
