package com.github.travelplannerapp.signin

import com.github.travelplannerapp.utils.SharedPreferencesUtils


interface SignInContract {
    interface View {
        fun showSignUp()
        fun signIn(credentials: SharedPreferencesUtils.Credentials)
        fun showSnackbar(message: String)
        fun showSnackbar(id: Int)
    }

    interface Presenter {
        fun onSignInClicked(email: String, password: String)
        fun onSignUpClicked()
        fun unsubscribe()
    }
}
