package com.github.travelplannerapp.signIn

import com.github.travelplannerapp.BasePresenter

class SignInPresenter(view: SignInContract.View) : BasePresenter<SignInContract.View>(view), SignInContract.Presenter {

    override fun signIn() {
        //example of the view and presenter flow
        view.showTravels()
    }
}
