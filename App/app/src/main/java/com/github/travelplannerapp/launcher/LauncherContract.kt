package com.github.travelplannerapp.launcher

interface LauncherContract {
    data class Credentials(val email: String, val token: String)

    interface View {
        fun showSignIn()
        fun showTravels()
        fun getCredentials(): Credentials
    }
    interface Presenter {
        fun redirect()
    }
}