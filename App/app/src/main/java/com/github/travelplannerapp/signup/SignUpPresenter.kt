package com.github.travelplannerapp.signup

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.SignUpRequest
import com.github.travelplannerapp.utils.PasswordUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpPresenter(view: SignUpContract.View) : BasePresenter<SignUpContract.View>(view), SignUpContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun signUp(email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) view.showSnackbar(R.string.sign_up_diff_passwords)

        val hashedPassword = PasswordUtils().hashPassword(password)
        if (hashedPassword == null) {
            view.showSnackbar(R.string.try_again)
        } else {
            compositeDisposable.add(CommunicationService.serverApi.register(SignUpRequest(email, hashedPassword))
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .subscribe(
                            { handleSignUpResponse() },
                            //TODO [Dorota] Display errors from server or server connection failure
                            { view.showSnackbar(R.string.server_connection_failure) }
                    ))
        }
    }

    override fun signIn() {
        view.showSignIn()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun handleSignUpResponse() {
        view.returnResultAndFinish(R.string.sign_up_successful)
    }
}
