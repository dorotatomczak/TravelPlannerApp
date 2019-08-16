package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.BasePresenter

class LauncherPresenter (view: LauncherContract.View) : BasePresenter<LauncherContract.View>(view), LauncherContract.Presenter {
    override fun redirect() {
        if (isLoggedIn()){
            view.showTravels()
        }
        else{
            view.showSignIn()
        }
    }
    
    private fun isLoggedIn(): Boolean {
        val (email, token) = view.getCredentials()
        return !(email.isNullOrEmpty() || token.isNullOrEmpty())
    }
}