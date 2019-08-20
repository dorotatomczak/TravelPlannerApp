package com.github.travelplannerapp.utils

import android.content.Context
import com.github.travelplannerapp.R


object SharedPreferencesUtils {
    fun getSessionCredentials(context: Context): SessionCredentials {
        val sharedPref = context.getSharedPreferences(context.resources.getString(R.string.auth_settings),
                Context.MODE_PRIVATE)
        return SessionCredentials(
                sharedPref.getInt(context.resources.getString(R.string.user_id_shared_pref), -1),
                sharedPref.getString(context.resources.getString(R.string.email_shared_pref),
                        "default").toString(),
                sharedPref.getString(context.resources.getString(R.string.auth_token_shared_pref),
                        "default").toString())
    }
}

class SessionCredentials(var userId: Int, var email: String, var authToken: String)