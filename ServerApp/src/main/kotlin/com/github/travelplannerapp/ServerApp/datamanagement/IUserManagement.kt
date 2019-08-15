package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginResponse
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginRequest

interface IUserManagement {
    fun verifyUser(email: String, auth: String): Boolean
    fun authenticateUser(loginRequest: JsonLoginRequest): JsonLoginResponse
    fun updateAuthorizationToken(loginRequest: JsonLoginRequest): String
    fun addUser(loginRequest: JsonLoginRequest): JsonLoginResponse
}