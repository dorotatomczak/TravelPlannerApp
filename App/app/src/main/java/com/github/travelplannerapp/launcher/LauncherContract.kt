package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.utils.SessionCredentials

interface LauncherContract {
    interface View {
        fun showSignIn()
        fun showTravels()
    }
    interface Presenter {
        fun redirect(credentials: SessionCredentials)
    }
}