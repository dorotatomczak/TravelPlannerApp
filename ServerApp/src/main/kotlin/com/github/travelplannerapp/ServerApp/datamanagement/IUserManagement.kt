package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginAnswer
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginRequest

interface IUserManagement {
    fun verifyUser(email: String, auth: String): Boolean
    fun authenticateUser(loginRequest: JsonLoginRequest): JsonLoginAnswer
    fun updateAuthorizationToken(loginRequest: JsonLoginRequest): String
    fun addUser(loginRequest: JsonLoginRequest): JsonLoginAnswer
}