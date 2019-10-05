package com.github.travelplannerapp.searchfriend

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.communication.model.UserInfo
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class SearchFriendPresenter(view: SearchFriendContract.View) : BasePresenter<SearchFriendContract.View>(view), SearchFriendContract.Presenter {
    private val compositeDisposable = CompositeDisposable()

    override fun addFriend(friend: UserInfo) {
        compositeDisposable.add(CommunicationService.serverApi.addFriend(SharedPreferencesUtils.getUserId(), friend.email)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { email -> handleAddTravelResponse(friend) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleAddTravelResponse(friend: UserInfo) {
        view.showSnackbar(R.string.friend_added)
    }

    private fun handleErrorResponse(error: Throwable) {
        view.showSnackbar(R.string.error)
    }

}
