package com.github.travelplannerapp.signin

interface SignInContract {
    interface View {
        fun showTravels()
        fun showSignUp()
    }

    interface Presenter {
        fun signIn()
        fun signUp()
    }
}
