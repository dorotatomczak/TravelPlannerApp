package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class LauncherPresenter (view: LauncherContract.View) : BasePresenter<LauncherContract.View>(view), LauncherContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun redirect(credentials: SharedPreferencesUtils.Credentials) {
        if (isLoggedIn(credentials)) {
            verifyAccessToken(credentials.token!!, credentials.userId)
        } else view.showSignIn()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun isLoggedIn(credentials: SharedPreferencesUtils.Credentials): Boolean {
        return !(credentials.userId == -1 && credentials.email.isNullOrEmpty() && credentials.token.isNullOrEmpty())
    }

    private fun verifyAccessToken(token: String, userId: Int) {
        compositeDisposable.add(CommunicationService.serverApi.authorize(token, userId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.statusCode == 200) it.data else throw ApiException(it.statusCode) }
                .subscribe(
                        { view.showTravels() },
                        { view.showSignIn() }
                ))
    }
}