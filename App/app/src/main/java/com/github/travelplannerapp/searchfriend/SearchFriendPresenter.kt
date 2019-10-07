package com.github.travelplannerapp.searchfriend

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.model.UserInfo
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class SearchFriendPresenter(view: SearchFriendContract.View) : BasePresenter<SearchFriendContract.View>(view), SearchFriendContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private var friends = ArrayList<UserInfo>()
    private var friendsToDeleteIds = mutableSetOf<Int>()

    override fun loadFriends() {
        compositeDisposable.add(CommunicationService.serverApi.getFriends(SharedPreferencesUtils.getUserId())
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { friends -> handleLoadFriendsResponse(friends) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun addFriend(friend: UserInfo) {
        compositeDisposable.add(CommunicationService.serverApi.addFriend(SharedPreferencesUtils.getUserId(), friend.email)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { email -> handleAddFriendResponse(friend) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun deleteFriends() {
        compositeDisposable.add(CommunicationService.serverApi.deleteFriends(
                SharedPreferencesUtils.getUserId(),
                friendsToDeleteIds)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { handleDeleteFriendsResponse() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadFriendsResponse(userFriends: List<UserInfo>) {
        friends = ArrayList(userFriends)
        view.onDataSetChanged()
        view.setLoadingIndicatorVisibility(false)
        view.showFriends()
    }

    private fun handleAddFriendResponse(friend: UserInfo) {
        view.showSnackbar(R.string.friend_added)
        view.onDataSetChanged()
        view.setLoadingIndicatorVisibility(false)
        view.showFriends()
    }

    private fun handleDeleteFriendsResponse() {
        friendsToDeleteIds.clear()
        loadFriends()
        view.showSnackbar(R.string.delete_friends_ok)
    }

    private fun handleErrorResponse(error: Throwable) {
        view.showSnackbar(R.string.error)
    }

    override fun getFriendsCount(): Int {
        return friends.size
    }

    override fun onBindFriendAtPosition(position: Int, itemView: SearchFriendContract.FriendItemView) {
        val friend = friends[position]
        itemView.setEmail(friend.email)
        itemView.setCheckbox()
    }

    override fun addFriendIdToDelete(position: Int) {
        friendsToDeleteIds.add(friends[position].id)
    }

    override fun removeFriendIdToDelete(position: Int) {
        friendsToDeleteIds.remove(friends[position].id)
    }

    override fun leaveActionMode() {
        view.onDataSetChanged()
        view.showNoActionMode()
    }

    override fun enterActionMode() {
        view.onDataSetChanged()
        view.showActionMode()
    }

    override fun onDeleteClicked() {
        if (friendsToDeleteIds.size > 0) {
            view.showConfirmationDialog()
        }
    }
}
