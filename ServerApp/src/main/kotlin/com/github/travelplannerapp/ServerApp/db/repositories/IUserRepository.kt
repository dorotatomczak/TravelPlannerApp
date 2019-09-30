package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.User

interface IUserRepository : IRepository<User> {
    fun getUserByEmail(email: String): User?
    fun getAllEmails(): MutableList<String>
    fun getAllFriendsByUserId(id: Int): MutableList<String>
}