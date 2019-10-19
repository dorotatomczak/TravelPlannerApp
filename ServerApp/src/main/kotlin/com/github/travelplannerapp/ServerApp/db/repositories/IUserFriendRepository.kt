package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.UserFriend

interface IUserFriendRepository : IRepository<UserFriend> {
    fun deleteUserFriendBinding(userId: Int, friendId: Int): Boolean
    fun ifUserFriendBindingExist(userId: Int, friendId: Int): Boolean
}