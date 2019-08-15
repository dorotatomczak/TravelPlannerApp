package com.github.travelplannerapp.signin

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonLoginResponse
import com.github.travelplannerapp.jsondatamodels.JsonLoginRequest
import com.github.travelplannerapp.jsondatamodels.LoginResponse
import com.github.travelplannerapp.utils.PasswordUtils
import com.google.gson.Gson

class SignInPresenter(view: SignInContract.View) : BasePresenter<SignInContract.View>(view), SignInContract.Presenter {

    lateinit var email: String

    override fun signUp() {
        view.showSignUp()
    }

    override fun signIn(email: String, password: String) {
        val requestInterface = CommunicationService.serverApi

        val hashedPassword = PasswordUtils().hashPassword(password)
        if (hashedPassword == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            this.email = email
            val requestBody = Gson().toJson(JsonLoginRequest(email, hashedPassword))
            view.authorize(requestInterface, requestBody, this::handleLoginResponse)
        }
    }

    override fun handleLoginResponse(jsonString: String) {
        val response = Gson().fromJson(jsonString, JsonLoginResponse::class.java)
        when (response.result) {
            LoginResponse.OK -> view.signIn(response.authorizationToken, email)
            LoginResponse.ERROR-> view.showSnackbar(R.string.sing_in_error)
        }
    }
}
