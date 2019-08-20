package com.github.travelplannerapp.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    private const val PREF_FILE_NAME = "PREF_FILE_NAME"
    private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    private const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
    private const val PREF_KEY_EMAIL = "PREF_KEY_EMAIL"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun clear(context: Context) {
        getSharedPreferences(context).edit().clear().apply()
    }

    fun getAccessToken(context: Context): String? {
        return getSharedPreferences(context).getString(PREF_KEY_ACCESS_TOKEN, null)
    }

    fun getUserId(context: Context): Int {
        return getSharedPreferences(context).getInt(PREF_KEY_USER_ID, -1)
    }

    fun getEmail(context: Context): String? {
        return getSharedPreferences(context).getString(PREF_KEY_EMAIL, null)
    }

    fun setAuthSettings(authSettings: AuthSettings, context: Context) {
        getSharedPreferences(context).edit()
                .putString(PREF_KEY_ACCESS_TOKEN, authSettings.token)
                .putInt(PREF_KEY_USER_ID, authSettings.userId)
                .putString(PREF_KEY_EMAIL, authSettings.email)
                .apply()
    }

    fun getAuthSettings(context: Context): AuthSettings {
        return AuthSettings(
                getAccessToken(context),
                getUserId(context),
                getEmail(context)
        )
    }

    data class AuthSettings(val token: String?, val userId: Int, val email: String?)
}