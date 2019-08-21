package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class LauncherPresenter (view: LauncherContract.View) : BasePresenter<LauncherContract.View>(view), LauncherContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun redirect(authSettings: SharedPreferencesUtils.AuthSettings) {
        if (isLoggedIn(authSettings)) {
            verifyAccessToken(authSettings.token!!, authSettings.userId)
        } else view.showSignIn()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
    
    private fun isLoggedIn(authSettings: SharedPreferencesUtils.AuthSettings): Boolean {
        return !(authSettings.userId == -1 && authSettings.email.isNullOrEmpty() && authSettings.token.isNullOrEmpty())
    }

    private fun verifyAccessToken(token: String, userId: Int) {
        compositeDisposable.add(CommunicationService.serverApi.authorize(token, userId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .subscribe(
                        { view.showTravels() },
                        //TODO [Dorota] Display errors from server or server connection failure
                        { view.showSignIn() }
                ))
    }
}