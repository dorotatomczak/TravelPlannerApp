package com.github.travelplannerapp.signup

import com.github.travelplannerapp.communication.ServerApi

interface SignUpContract {

    interface View {
        fun showSignIn()
        fun showSnackbar(messageCode: Int)
        fun sendSignUpRequest(requestInterface: ServerApi, jsonLoginRequest: String, handleResponse: (jsonString: String) -> Unit)
        fun returnResultAndFinish(messageCode: Int)
    }

    interface Presenter {
        fun signUp(email: String, password: String, confirmPassword: String)
        fun signIn()
        fun handleSignUpResponse(jsonString: String)
    }
}