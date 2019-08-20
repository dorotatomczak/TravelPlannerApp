package com.github.travelplannerapp.signup

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonLoginAnswer
import com.github.travelplannerapp.jsondatamodels.JsonLoginRequest
import com.github.travelplannerapp.jsondatamodels.LOGIN_ANSWER
import com.github.travelplannerapp.util.PasswordUtils
import com.google.gson.Gson

class SignUpPresenter(view: SignUpContract.View) : BasePresenter<SignUpContract.View>(view), SignUpContract.Presenter {

    lateinit var email: String

    override fun signUp(email: String, password: String, confirmPassword: String) {
        val requestInterface = CommunicationService.serverApi

        if (password != confirmPassword) {
            view.showSnackbar(R.string.sign_up_diff_passwords)
        }

        val hashedPassword = PasswordUtils().hashPassword(password)
        if (hashedPassword == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            this.email = email
            val requestBody = Gson().toJson(JsonLoginRequest(email, hashedPassword))
            view.sendSignUpRequest(requestInterface, requestBody, this::handleSignUpResponse)
        }
    }

    override fun signIn() {
        view.showSignIn()
    }

    override fun handleSignUpResponse(jsonString: String) {
        val answer = Gson().fromJson(jsonString, JsonLoginAnswer::class.java)
        when (answer.result) {
            LOGIN_ANSWER.OK -> view.returnResultAndFinish(R.string.sign_up_successful)
            LOGIN_ANSWER.ERROR -> view.showSnackbar(R.string.sign_up_email_error)
        }
    }
}
