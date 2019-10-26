package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.UserFriend
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
    fun addFriend(userId: Int, friendId: Int): UserFriend
    fun deleteFriends(userId: Int, friendsIds: MutableSet<Int>)
    fun findMatchingEmails(query: String): MutableList<UserInfo>
    fun getAllFriendsByUserId(userId: Int): MutableList<UserInfo>
    fun getFriendsWithCheckAccessToTravel(userId: Int, travelId: Int,ifHaveAccess:Boolean): MutableList<UserInfo>
}
