package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.communication.commonmodel.SignInRequest
import com.github.travelplannerapp.communication.commonmodel.SignUpRequest
import com.github.travelplannerapp.communication.commonmodel.UserInfo

interface IUserManagement {
    fun getUserId(token: String): Int
    fun verifyUser(token: String)
    fun authenticateUser(request: SignInRequest): Int
    fun updateAuthorizationToken(id: Int, request: SignInRequest): String
    fun addUser(request: SignUpRequest)
    fun updateUser(id: Int, changes: MutableMap<String, Any?>)
    fun addFriend(userId: Int, friend: UserInfo): UserInfo
    fun deleteFriends(userId: Int, friendsIds: MutableSet<Int>)
    fun findMatchingEmails(userId: Int, query: String): MutableList<UserInfo>
    fun getAllFriendsByUserId(userId: Int): MutableList<UserInfo>
    fun getFriendsBySharedTravel(userId: Int, travelId: Int, selectFriendsWithAccess: Boolean): MutableList<UserInfo>
}
