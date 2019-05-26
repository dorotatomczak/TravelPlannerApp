package com.github.travelplannerapp.signIn

interface SignInContract {
    interface View {
        fun showTravels()
    }

    interface Presenter {
        fun signIn()
    }
}
