package com.github.travelplannerapp.login

import com.github.travelplannerapp.BasePresenter

class LoginPresenter(view: LoginContract.View) : BasePresenter<LoginContract.View>(view), LoginContract.Presenter {

    override fun signIn() {
        //example of the view and presenter flow
        view.showTravels()
    }
}
