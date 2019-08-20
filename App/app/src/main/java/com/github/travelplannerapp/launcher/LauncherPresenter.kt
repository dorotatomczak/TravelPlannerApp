package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.utils.SessionCredentials

class LauncherPresenter (view: LauncherContract.View) : BasePresenter<LauncherContract.View>(view), LauncherContract.Presenter {

    override fun redirect(credentials: SessionCredentials) {
        if (isLoggedIn(credentials)) view.showTravels()
        else view.showSignIn()
    }
    
    private fun isLoggedIn(credentials: SessionCredentials): Boolean {
        return (credentials.userId != -1 && credentials.email != "default" && credentials.authToken != "default")
    }
}