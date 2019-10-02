package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
import com.github.travelplannerapp.ServerApp.datamodels.Response
import com.github.travelplannerapp.ServerApp.datamodels.SignInRequest
import com.github.travelplannerapp.ServerApp.datamodels.SignInResponse
import com.github.travelplannerapp.ServerApp.datamodels.SignUpRequest
import com.github.travelplannerapp.ServerApp.db.dao.UserFriend
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerUserController {

    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var userRepository: UserRepository


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

    @GetMapping("user-management/getusersemails")
    fun getUsersEmails(): Response<MutableList<String>> {
        val emails = userManagement.getUsersEmails()
        return Response(ResponseCode.OK, emails)
    }


    @GetMapping("users/{userId}/friends")
    fun getFriends(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int
    ): Response<List<String>> {
        userManagement.verifyUser(token)
        val friends = userRepository.getAllFriendsByUserId(userId)
        return Response(ResponseCode.OK, friends)
    }

    @PostMapping("users/{userId}/friends")
    fun addFriend(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @RequestBody friendEmail: String
    ): Response<Boolean> {
        userManagement.verifyUser(token)
        val newTravel = userManagement.addFriend(userId, friendEmail)
        return Response(ResponseCode.OK, newTravel)
    }
//    @DeleteMapping("users/{userId}/friends")
//    fun deleteFriends(
//            @RequestHeader("authorization") token: String,
//            @PathVariable userId: Int,
//            @RequestBody friendsIds: MutableSet<Int>
//    ): Response<Unit> {
//        userManagement.verifyUser(token)
//        userManagement.deleteFriends(userId, friendIds)
//        return Response(ResponseCode.OK, Unit)
//    }
}
