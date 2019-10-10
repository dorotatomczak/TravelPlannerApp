package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.SignInRequest
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.SignUpRequest

interface IUserManagement {
    fun getUserId(token: String): Int
    fun verifyUser(token: String)
    fun authenticateUser(request: SignInRequest): Int
    fun updateAuthorizationToken(id: Int, request: SignInRequest): String
    fun addUser(request: SignUpRequest)
    fun updateUser(id: Int, changes: MutableMap<String, Any?>)
}
