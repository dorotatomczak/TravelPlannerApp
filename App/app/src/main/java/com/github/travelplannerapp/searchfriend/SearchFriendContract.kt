package com.github.travelplannerapp.searchfriend

import com.github.travelplannerapp.deleteactionmode.DeleteContract

interface SearchFriendContract {
    interface View : DeleteContract.View {
        fun showSnackbar(messageCode: Int)
        fun onDataSetChanged()
        fun showFriends()
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
    }

    interface FriendItemView : DeleteContract.ItemView {
        fun setEmail(email: String)
    }

    interface Presenter : DeleteContract.Presenter {
        fun addFriend(friendEmail: String)
        fun loadFriends()
        fun getFriendsCount(): Int
        fun addFriendIdToDelete(position: Int)
        fun removeFriendIdToDelete(position: Int)
        fun onBindFriendAtPosition(position: Int, itemView: FriendItemView)
        fun deleteFriends()
    }
}
