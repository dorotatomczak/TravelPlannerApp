package com.github.travelplannerapp.signup

interface SignUpContract {

    interface View {
        fun showSignIn()
        fun showSnackbar(messageCode: Int)
        fun returnResultAndFinish(messageCode: Int)
    }

    interface Presenter {
        fun onSignUpClicked(email: String, password: String, confirmPassword: String)
        fun onSignInClicked()
        fun unsubscribe()
    }
}
