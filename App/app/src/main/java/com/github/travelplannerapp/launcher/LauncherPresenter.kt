package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.utils.SharedPreferencesUtils

class LauncherPresenter (view: LauncherContract.View) : BasePresenter<LauncherContract.View>(view), LauncherContract.Presenter {

    override fun redirect(authSettings: SharedPreferencesUtils.AuthSettings) {
        if (isLoggedIn(authSettings)) view.showTravels()
        else view.showSignIn()
    }
    
    private fun isLoggedIn(authSettings: SharedPreferencesUtils.AuthSettings): Boolean {
        return !(authSettings.userId == -1 && authSettings.email.isNullOrEmpty() && authSettings.token.isNullOrEmpty())
    }
}