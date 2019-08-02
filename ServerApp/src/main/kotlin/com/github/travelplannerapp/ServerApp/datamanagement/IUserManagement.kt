package com.github.travelplannerapp.ServerApp.datamanagement

interface IUserManagement {
    fun verifyUser(email: String, auth: String): Boolean
}