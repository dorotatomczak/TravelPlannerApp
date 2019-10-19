package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.UserInfo
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
import com.github.travelplannerapp.communication.commonmodel.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerUserController {

    @Autowired
    lateinit var userManagement: UserManagement

    @PostMapping("user-management/authorize")
    fun authorize(@RequestHeader("authorization") token: String): Response<Unit> {
        userManagement.verifyUser(token)
        return Response(ResponseCode.OK, Unit)
    }

    @PostMapping("user-management/authenticate")
    fun authenticate(@RequestBody request: SignInRequest): Response<SignInResponse> {
        val userId = userManagement.authenticateUser(request)
        val token = userManagement.updateAuthorizationToken(userId, request)
        return Response(ResponseCode.OK, SignInResponse(token, userId))
    }

    @PostMapping("user-management/register")
    fun register(@RequestBody request: SignUpRequest): Response<Unit> {
        userManagement.addUser(request)
        return Response(ResponseCode.OK, Unit)
    }


    @GetMapping("users/{userId}/friends")
    fun getFriends(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int
    ): Response<List<UserInfo>> {
        userManagement.verifyUser(token)
        val friends = userManagement.getAllFriendsByUserId(userId)
        return Response(ResponseCode.OK, friends)
    }

    @PostMapping("users/{userId}/friends")
    fun addFriend(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @RequestBody friendEmail: String
    ): Response<UserInfo> {
        userManagement.verifyUser(token)
        val response = userManagement.addFriend(userId, friendEmail)
        var friend: UserInfo = UserInfo(response.friendId!!, friendEmail)
        return Response(ResponseCode.OK, friend)
    }

    @GetMapping("users/{userId}/usersemails")
    fun findMatchingEmails(
            @PathVariable userId: Int,
            @RequestParam("query") query: String
    ): Response<MutableList<UserInfo>> {
        try {
            val users = userManagement.findMatchingEmails(userId,query)
            return Response(ResponseCode.OK, users)
        } catch (ex: Exception) {
            throw SearchNoItemsException(ex.localizedMessage)
        }
    }

    @DeleteMapping("users/{userId}/friends")
    fun deleteFriends(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @RequestBody friendsIds: MutableSet<Int>
    ): Response<Unit> {
        userManagement.verifyUser(token)
        userManagement.deleteFriends(userId, friendsIds)
        return Response(ResponseCode.OK, Unit)
    }
}
