package com.github.travelplannerapp.signin


import retrofit2.http.Body
import io.reactivex.Observable
import retrofit2.http.POST


interface SignInContract {
    interface View {
        fun authorize(requestInterface: ServerAPI, jsonLoginRequest: String, handleResponse: (jsonString: String) -> Unit)
        fun showSignUp()
        fun signIn(auth: String, email: String)
        fun showSnackbar(message: String)
        fun showSnackbar(id: Int)
    }

    interface Presenter {
        fun signIn(email: String, password: String)
        fun signUp()
        fun handleLoginResponse(jsonString: String)
    }

    interface ServerAPI {
        @POST("/authenticate")
        fun authenticate(@Body jsonString: String): Observable<String>
    }
}
