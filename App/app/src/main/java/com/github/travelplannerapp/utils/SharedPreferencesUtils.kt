package com.github.travelplannerapp.utils

import android.content.Context
import android.content.SharedPreferences

private const val PREF_FILE_NAME = "AUTH_SETTINGS"
private const val PREF_KEY_EMAIL = "PREF_KEY_EMAIL"
private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

class SharedPreferencesUtils (context: Context) {

    var sharedPrefs: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun getAccessToken(): String? {
        return sharedPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
    }

    fun setAccessToken(token: String) {
        sharedPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()
    }

    fun getEmail(): String? {
        return sharedPrefs.getString(PREF_KEY_EMAIL, null)
    }

    fun setEmail(email: String) {
        sharedPrefs.edit().putString(PREF_KEY_EMAIL, email).apply()
    }
}