package com.github.travelplannerapp.launcher

interface LauncherContract {
    interface View {
        fun showSignIn()
        fun showTravels()
    }
    interface Presenter {
        fun redirect()
    }
}