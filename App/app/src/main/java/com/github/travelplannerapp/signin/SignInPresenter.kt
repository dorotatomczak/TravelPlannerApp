package com.github.travelplannerapp.signin

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.commonmodel.SignInRequest
import com.github.travelplannerapp.communication.commonmodel.SignInResponse
import com.github.travelplannerapp.utils.PasswordUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class SignInPresenter(view: SignInContract.View) : BasePresenter<SignInContract.View>(view), SignInContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    lateinit var email: String

    override fun onSignUpClicked() {
        view.showSignUp()
    }

    override fun onSignInClicked(email: String, password: String) {
        val hashedPassword = PasswordUtils.hashPassword(password)
        if (hashedPassword == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            this.email = email

            compositeDisposable.add(CommunicationService.serverApi.authenticate(SignInRequest(email, hashedPassword))
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                    .subscribe(
                            { response -> handleSignInResponse(response) },
                            { error -> handleErrorResponse(error) }
                    ))
        }
    }

    private fun handleSignInResponse(response: SignInResponse) {
        view.signIn(SharedPreferencesUtils.Credentials(response.token, response.userId, email))
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}
