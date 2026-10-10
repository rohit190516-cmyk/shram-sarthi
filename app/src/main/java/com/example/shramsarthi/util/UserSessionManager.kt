package com.example.shramsarthi.util

import android.content.Context
import android.util.Log
import com.example.shramsarthi.model.UserRole

/**
 * Manages locally cached session attributes (e.g., active persona role, cached UID)
 * in SharedPreferences to provide immediate offline resilience, eliminate startup race
 * conditions, and keep navigation state and Firestore role strictly synchronized.
 */
object UserSessionManager {

    private const val TAG = "UserSessionManager"
    private const val PREFS_NAME = "shram_sarthi_session_prefs"
    private const val KEY_SAVED_ROLE = "saved_user_role"
    private const val KEY_CACHED_UID = "cached_user_uid"

    /**
     * Persists the active user role locally.
     */
    fun saveUserRole(context: Context, role: UserRole) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_SAVED_ROLE, role.name)
            .apply()
        Log.d(TAG, "saveUserRole: Persisted role=${role.name}")
    }

    /**
     * Retrieves the locally cached user role, if any.
     */
    fun getUserRole(context: Context): UserRole? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val roleStr = prefs.getString(KEY_SAVED_ROLE, null) ?: return null
        return runCatching { UserRole.valueOf(roleStr) }.getOrNull()
    }

    /**
     * Saves the cached authenticated UID.
     */
    fun saveCachedUid(context: Context, uid: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CACHED_UID, uid).apply()
    }

    /**
     * Retrieves the cached authenticated UID.
     */
    fun getCachedUid(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CACHED_UID, null)
    }

    /**
     * Clears local session cache upon user logout.
     */
    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Log.d(TAG, "clearSession: Session cache cleared")
    }
}
