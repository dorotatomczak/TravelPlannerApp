package com.github.travelplannerapp.login

interface LoginContract {
    interface View {
        fun showTravels()
    }

    interface Presenter {
        fun signIn()
    }
}
