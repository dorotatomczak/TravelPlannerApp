package com.github.travelplannerapp.signin

interface SignInContract {
    interface View {
        fun showTravels()
    }

    interface Presenter {
        fun signIn()
    }
}
