package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.exceptions.ApiException
import com.github.travelplannerapp.ServerApp.exceptions.AuthorizationException
import com.github.travelplannerapp.ServerApp.exceptions.EmailAlreadyExistsException
import com.github.travelplannerapp.ServerApp.exceptions.WrongCredentialsException
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


    @PostMapping("/authorize")
    fun authorize(@RequestHeader("authorization") token: String, userId: Int): Response<Void> {
        userManagement.verifyUser(userId, token)
        return Response(200, null)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: SignInRequest): Response<SignInResponse> {
        val userId = userManagement.authenticateUser(request)
        val token = userManagement.updateAuthorizationToken(request)
        return Response(200, SignInResponse(token, userId))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: SignUpRequest): Response<Void> {
        userManagement.addUser(request)
        return Response(200, null)
    }

    @ExceptionHandler(AuthorizationException::class, WrongCredentialsException::class, EmailAlreadyExistsException::class)
    fun handleApiExceptions(exception: ApiException): Response<Any> {
        return Response(exception.code, null)
    }

    @ExceptionHandler(Exception::class)
    fun handlePredefinedExceptions(exception: Exception): Response<Any> {
        return Response(999, null)
    }
}