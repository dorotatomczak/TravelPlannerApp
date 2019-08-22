package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import com.github.travelplannerapp.ServerApp.jsondatamodels.*
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerUserController {

    @Autowired
    lateinit var userTravelRepository: UserTravelRepository
    @Autowired
    lateinit var userManagement: UserManagement


    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: String): String {
        val loginRequest = Gson().fromJson(request, JsonLoginRequest::class.java)

        val jsonLoginAnswer = userManagement.authenticateUser(loginRequest)
        if (jsonLoginAnswer.result != LOGIN_ANSWER.OK) {
            return Gson().toJson(jsonLoginAnswer)
        }

        val authToken = userManagement.updateAuthorizationToken(loginRequest)
        jsonLoginAnswer.authorizationToken = authToken

        return Gson().toJson(jsonLoginAnswer)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: String): String {
        val loginRequest = Gson().fromJson(request, JsonLoginRequest::class.java)
        val jsonLoginAnswer = userManagement.addUser(loginRequest)

        return Gson().toJson(jsonLoginAnswer)
    }
}