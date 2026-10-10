package com.example.shramsarthi.util

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.util.Locale

/**
 * Supported app languages for Shram Sarthi.
 */
enum class AppLanguage(val code: String, val titleInLanguage: String, val englishName: String) {
    HINDI("hi", "हिन्दी", "Hindi"),
    ENGLISH("en", "English", "English");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return when (code?.lowercase()) {
                "en" -> ENGLISH
                else -> HINDI // Default to Hindi
            }
        }
    }
}

/**
 * Singleton managing persisted language preference across restarts, login/logout, and role switches.
 */
object LanguageManager {
    private const val PREFS_NAME = "shram_sarthi_language_prefs"
    private const val KEY_LANGUAGE = "selected_language"
    private const val KEY_LANGUAGE_SELECTED = "has_selected_language"

    private val _currentLanguageState = mutableStateOf(AppLanguage.HINDI)
    val currentLanguageState: State<AppLanguage> = _currentLanguageState

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedCode = prefs.getString(KEY_LANGUAGE, "hi")
        val language = AppLanguage.fromCode(savedCode)
        _currentLanguageState.value = language
        applyLocale(context, language)
    }

    fun hasUserSelectedLanguage(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LANGUAGE_SELECTED, false)
    }

    fun markLanguageSelected(context: Context, language: AppLanguage) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_LANGUAGE, language.code)
            .putBoolean(KEY_LANGUAGE_SELECTED, true)
            .apply()
        _currentLanguageState.value = language
        applyLocale(context, language)
    }

    fun setLanguage(context: Context, language: AppLanguage) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_LANGUAGE, language.code)
            .putBoolean(KEY_LANGUAGE_SELECTED, true)
            .apply()
        _currentLanguageState.value = language
        applyLocale(context, language)
    }

    fun getCurrentLanguage(): AppLanguage = _currentLanguageState.value

    fun applyLocale(context: Context, language: AppLanguage) {
        val locale = Locale(language.code, "IN")
        Locale.setDefault(locale)
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.HINDI }

/**
 * Composable helper returning the localized string resource based on active AppLanguage.
 */
@Composable
@ReadOnlyComposable
fun localizedString(resId: Int): String {
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val config = Configuration(context.resources.configuration)
    config.setLocale(Locale(language.code, "IN"))
    val localizedContext = context.createConfigurationContext(config)
    return localizedContext.resources.getString(resId)
}
