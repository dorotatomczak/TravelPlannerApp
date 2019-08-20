package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.util.SharedPreferencesUtil

interface LauncherContract {
    interface View {
        fun showSignIn()
        fun showTravels()
    }
    interface Presenter {
        fun redirect(authSettings: SharedPreferencesUtil.AuthSettings)
    }
}