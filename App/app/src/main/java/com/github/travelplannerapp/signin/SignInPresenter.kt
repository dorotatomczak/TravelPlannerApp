package com.github.travelplannerapp.signin

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.SignInRequest
import com.github.travelplannerapp.communication.model.SignInResponse
import com.github.travelplannerapp.utils.PasswordUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class SignInPresenter(view: SignInContract.View) : BasePresenter<SignInContract.View>(view), SignInContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
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

            compositeDisposable.add(CommunicationService.serverApi.authenticate(SignInRequest(email, hashedPassword))
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .subscribe(
                            { response -> handleSignInResponse(response) },
                            //TODO [Dorota] Display errors from server or server connection failure
                            { view.showSnackbar(R.string.server_connection_failure) }
                    ))
        }
    }

    private fun handleSignInResponse(response: SignInResponse) {
        view.signIn(SharedPreferencesUtils.AuthSettings(response.token, response.userId, email))
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}
