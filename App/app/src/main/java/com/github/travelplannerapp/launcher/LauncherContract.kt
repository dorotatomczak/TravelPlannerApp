package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.utils.SharedPreferencesUtils

interface LauncherContract {
    interface View {
        fun showSignIn()
        fun showTravels()
    }
    interface Presenter {
        fun redirect(credentials: SharedPreferencesUtils.Credentials)
        fun unsubscribe()
    }
}