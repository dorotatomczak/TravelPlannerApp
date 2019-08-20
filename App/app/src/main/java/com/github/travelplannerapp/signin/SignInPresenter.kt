package com.github.travelplannerapp.signin

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonLoginAnswer
import com.github.travelplannerapp.jsondatamodels.JsonLoginRequest
import com.github.travelplannerapp.jsondatamodels.LOGIN_ANSWER
import com.github.travelplannerapp.util.PasswordUtils
import com.github.travelplannerapp.util.SharedPreferencesUtil
import com.google.gson.Gson

class SignInPresenter(view: SignInContract.View) : BasePresenter<SignInContract.View>(view), SignInContract.Presenter {

    lateinit var email: String

    override fun signUp() {
        view.showSignUp()
    }

    override fun signIn(email: String, password: String) {
        val hashedPassword = PasswordUtils().hashPassword(password)
        if (hashedPassword == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            this.email = email
            val requestBody = Gson().toJson(JsonLoginRequest(email, hashedPassword))
            view.authorize(CommunicationService.serverApi, requestBody, this::handleLoginResponse)
        }
    }

    override fun handleLoginResponse(jsonString: String) {
        val answer = Gson().fromJson(jsonString, JsonLoginAnswer::class.java)
        when (answer.result) {
            LOGIN_ANSWER.OK -> view.signIn(SharedPreferencesUtil.AuthSettings(answer.authorizationToken, answer.userId, email))
            LOGIN_ANSWER.ERROR -> view.showSnackbar(R.string.sign_in_error)
        }
    }
}
