package com.github.travelplannerapp.searchfriend

import com.github.travelplannerapp.communication.model.UserInfo

interface SearchFriendContract {
    interface View {
        fun showSnackbar(messageCode: Int)
        fun showAddFriend(friend: UserInfo)
    }

    interface Presenter {
        fun addFriend(friend: UserInfo)
    }
}
