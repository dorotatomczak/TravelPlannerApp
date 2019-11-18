package com.github.travelplannerapp.signup

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.commonmodel.SignUpRequest
import com.github.travelplannerapp.utils.PasswordUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpPresenter(view: SignUpContract.View) : BasePresenter<SignUpContract.View>(view), SignUpContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun onSignUpClicked(email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            view.showSnackbar(R.string.sign_up_diff_passwords)
            return
        }

        if(!verifyEmail(email)){
            view.showSnackbar(R.string.email_incorrect)
            return
        }

        if(!verifyPassword(password)){
            view.showSnackbar(R.string.password_too_simple)
            return
        }

        val hashedPassword = PasswordUtils.hashPassword(password)
        if (hashedPassword == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            compositeDisposable.add(CommunicationService.serverApi.register(SignUpRequest(email, hashedPassword))
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                    .subscribe(
                            { handleSignUpResponse() },
                            { error -> handleErrorResponse(error) }
                    ))
        }
    }

    override fun onSignInClicked() {
        view.showSignIn()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun handleSignUpResponse() {
        view.returnResultAndFinish(R.string.sign_up_successful)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

    private fun verifyPassword(password: String): Boolean {
        if (password.length < 8) return false
        if ("[a-z]".toRegex().find(password) == null) return false
        if ("[A-Z]".toRegex().find(password) == null) return false
        if ("[0-9]".toRegex().find(password) == null) return false

        return true
    }

    private fun verifyEmail(email: String): Boolean =
            email.matches("[^\\s]+@[^\\s]+\\.[^\\s]+".toRegex())
}
