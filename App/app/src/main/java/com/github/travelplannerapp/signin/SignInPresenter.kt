package com.github.travelplannerapp.signin

import android.util.Log
import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonLoginAnswer
import com.github.travelplannerapp.jsondatamodels.JsonLoginRequest
import com.github.travelplannerapp.jsondatamodels.LOGIN_ANSWER
import com.github.travelplannerapp.utils.PasswordUtils
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SignInPresenter(view: SignInContract.View) : BasePresenter<SignInContract.View>(view), SignInContract.Presenter {

    private var server = CommunicationService
    lateinit var email: String

    override fun signUp() {
        view.showSignUp()
    }

    override fun signIn(email: String, password: String) {
        val requestInterface = Retrofit.Builder()
                .baseUrl(server.getUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(SignInContract.ServerAPI::class.java)

        val pwd = PasswordUtils().hashPassword(password)
        if (pwd == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            this.email = email
            val requestBody = Gson().toJson(JsonLoginRequest(email, password))
            view.authorize(requestInterface, requestBody, this::handleLoginResponse)
        }
    }

    override fun handleLoginResponse(jsonString: String) {
        val answer = Gson().fromJson(jsonString, JsonLoginAnswer::class.java)
        if (answer.result == LOGIN_ANSWER.OK) {
            view.signIn(answer.authorizationToken, email)
        } else {
            view.showSnackbar(answer.authorizationToken)
        }

    }
}
