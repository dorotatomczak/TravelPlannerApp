package com.github.travelplannerapp.signup

import com.github.travelplannerapp.BasePresenter

class SignUpPresenter(view: SignUpContract.View) : BasePresenter<SignUpContract.View>(view), SignUpContract.Presenter {

    override fun signUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn() {
        view.showSignIn()
    }
}