package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.datamodels.UserInfo
import com.github.travelplannerapp.ServerApp.db.dao.User

interface IUserRepository : IRepository<User> {
    fun getUserByEmail(email: String): User?
    fun getAllFriendsByUserId(id: Int): MutableList<String>
    fun findEmails(s: String):MutableList<UserInfo>
}