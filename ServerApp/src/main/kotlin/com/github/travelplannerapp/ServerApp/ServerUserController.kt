package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
import com.github.travelplannerapp.ServerApp.jsondatamodels.Response
import com.github.travelplannerapp.ServerApp.jsondatamodels.SignInRequest
import com.github.travelplannerapp.ServerApp.jsondatamodels.SignInResponse
import com.github.travelplannerapp.ServerApp.jsondatamodels.SignUpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerUserController {

    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var userRepository: UserRepository


    @PostMapping("/authorize")
    fun authorize(@RequestHeader("authorization") token: String): Response<Unit> {
        userManagement.verifyUser(token)
        return Response(ResponseCode.OK, Unit)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: SignInRequest): Response<SignInResponse> {
        val userId = userManagement.authenticateUser(request)
        val token = userManagement.updateAuthorizationToken(userId, request)
        return Response(ResponseCode.OK, SignInResponse(token, userId))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: SignUpRequest): Response<Unit> {
        userManagement.addUser(request)
        return Response(ResponseCode.OK, Unit)
    }
    @GetMapping("/getusersemails")
    fun getUsersEmails(): Response<MutableList<String>>{
        val emails = userManagement.getUsersEmails()
        return Response(ResponseCode.OK, emails)
    }

    @GetMapping("/getuserfriends")
    fun getUserFriends(@RequestHeader("authorization") token: String): Response<List<String>>{
        userManagement.verifyUser(token)
        val userId = userManagement.getUserId(token)
        val friends = userRepository.getAllFriendsByUserId(userId)
        return Response(ResponseCode.OK, friends)
    }
}