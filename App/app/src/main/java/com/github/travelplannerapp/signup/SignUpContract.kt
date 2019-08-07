package com.github.travelplannerapp.signup

import com.github.travelplannerapp.communication.ServerApi

interface SignUpContract {
    interface View{
        fun showSignIn()
        fun signUp()
        fun showSnackbar(message: String, listener: android.view.View.OnClickListener?)
        fun showSnackbar(id: Int, listener: android.view.View.OnClickListener?)
        fun sendSignUpRequest(requestInterface: ServerApi, jsonLoginRequest: String, handleResponse: (jsonString: String) -> Unit)
    }

    interface Presenter {
        fun signUp(email: String, password: String, confirmPassword: String)
        fun signIn()
        fun handleSignUpResponse(jsonString: String)
    }
}