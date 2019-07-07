package com.github.travelplannerapp.signup

interface SignUpContract {
    interface View{
        fun showSignIn()
    }

    interface Presenter {
        fun signUp()
        fun signIn()
    }
}