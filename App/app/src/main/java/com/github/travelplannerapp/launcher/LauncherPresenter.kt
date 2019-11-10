package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable

class LauncherPresenter(view: LauncherContract.View) : BasePresenter<LauncherContract.View>(view), LauncherContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun redirect(credentials: SharedPreferencesUtils.Credentials) {
        if (isSignedIn(credentials)) {
            verifyAccessToken()
        } else view.showSignIn()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun isSignedIn(credentials: SharedPreferencesUtils.Credentials): Boolean =
            !(credentials.userId == -1 || credentials.email.isNullOrEmpty() || credentials.token.isNullOrEmpty())

    private fun fetchRecommendedPlaces() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    token?.let { send(it) }
                })
    }

    private fun send(token: String) {
        compositeDisposable.add(CommunicationService.serverApi.fetchRecommendations(SharedPreferencesUtils.getUserId(), token)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                .subscribe(
                        {},
                        {}
                ))
    }

    private fun verifyAccessToken() {
        compositeDisposable.add(CommunicationService.serverApi.authorize()
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                .subscribe(
                        {
                            fetchRecommendedPlaces()
                            view.showTravels()
                        },
                        { view.showSignIn() }
                ))
    }

}
