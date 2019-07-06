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

    //TODO([Dorota] check in SharedPreferences if user is already logged in)
    private fun isLoggedIn(): Boolean {
        return false
    }
}