package com.example.shramsarthi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.shramsarthi.ui.ShramSarthiApp
import com.example.shramsarthi.ui.screens.WelcomeScreen
import com.example.shramsarthi.ui.theme.ShramSarthiTheme
import com.example.shramsarthi.util.LanguageManager
import com.example.shramsarthi.util.LocalAppLanguage

import android.content.pm.ApplicationInfo
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        LanguageManager.init(this)

        try {
            FirebaseApp.initializeApp(this)
            val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            val appCheck = FirebaseAppCheck.getInstance()
            val providerFactory: AppCheckProviderFactory = if (isDebuggable) {
                try {
                    val debugClass = Class.forName("com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory")
                    val getInstanceMethod = debugClass.getMethod("getInstance")
                    getInstanceMethod.invoke(null) as AppCheckProviderFactory
                } catch (_: Throwable) {
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                }
            } else {
                PlayIntegrityAppCheckProviderFactory.getInstance()
            }
            appCheck.installAppCheckProviderFactory(providerFactory)
        } catch (_: Exception) {
            // Handled if already installed or in test environments
        }

        setContent {
            val currentLanguage by LanguageManager.currentLanguageState
            CompositionLocalProvider(LocalAppLanguage provides currentLanguage) {
                ShramSarthiTheme {
                    ShramSarthiApp()
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun WelcomeScreenPreview() {
    ShramSarthiTheme {
        WelcomeScreen(
            onGetStartedClick = {},
            onExploreRolesClick = {}
        )
    }
}