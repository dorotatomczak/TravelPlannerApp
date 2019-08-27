package com.github.travelplannerapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


object SharedPreferencesUtils {

    lateinit var sharedPref: SharedPreferences

    private const val PREF_FILE_NAME = "PREF_AUTHENTICATION"
    private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    private const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
    private const val PREF_KEY_EMAIL = "PREF_KEY_EMAIL"

    fun initialize(context: Context){
        sharedPref = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
    }
    fun clear() {
        sharedPref.edit().clear().apply()
    }

    fun getAccessToken(): String? {
        return sharedPref.getString(PREF_KEY_ACCESS_TOKEN, null)
    }

    fun getUserId(): Int {
        return sharedPref.getInt(PREF_KEY_USER_ID, -1)
    }

    fun getEmail(): String? {
        return sharedPref.getString(PREF_KEY_EMAIL, null)
    }

    fun setCredentials(credentials: Credentials) {
        sharedPref.edit()
                .putString(PREF_KEY_ACCESS_TOKEN, credentials.token)
                .putInt(PREF_KEY_USER_ID, credentials.userId)
                .putString(PREF_KEY_EMAIL, credentials.email)
                .apply()
    }

    fun getCredentials(): Credentials {
        return Credentials(
                getAccessToken(),
                getUserId(),
                getEmail()
        )
    }

    data class Credentials(val token: String?, val userId: Int, val email: String?)
}