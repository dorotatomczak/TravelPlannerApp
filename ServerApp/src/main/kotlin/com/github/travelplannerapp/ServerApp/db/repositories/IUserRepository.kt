package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.User
import java.sql.Timestamp

interface IUserRepository: IRepository<User> {

    fun getUserByEmail(email: String): User?

    fun updateUserAuthByEmail(email: String, authToken: String, expirationDate: Timestamp)
}