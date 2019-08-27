package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.jsondatamodels.SignInRequest
import com.github.travelplannerapp.ServerApp.jsondatamodels.SignUpRequest

interface IUserManagement {
    fun verifyUser(userId: Int, auth: String)

    fun authenticateUser(request: SignInRequest): Int

    fun updateAuthorizationToken(request: SignInRequest): String

    fun addUser(request: SignUpRequest)

    fun updateUser(id: Int, changes: MutableMap<String, Any?>)
}