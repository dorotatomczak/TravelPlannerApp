package com.github.travelplannerapp.signin


import com.github.travelplannerapp.communication.ServerApi


interface SignInContract {
    interface View {
        fun authorize(requestInterface: ServerApi, jsonLoginRequest: String, handleResponse: (jsonString: String) -> Unit)
        fun showSignUp()
        fun signIn(auth: String, email: String, userId: String)
        fun showSnackbar(message: String)
        fun showSnackbar(id: Int)
    }

    interface Presenter {
        fun signIn(email: String, password: String)
        fun signUp()
        fun handleLoginResponse(jsonString: String)
    }
}
