package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UserManagement : IUserManagement {
    @Autowired
    lateinit var userRepository: UserRepository
    override fun verifyUser(email: String, auth: String): Boolean{
        val user = userRepository.getUserByEmail(email)
        return (user?.authToken == auth
                && user.expirationDate.before(java.sql.Date.valueOf(LocalDate.now())))
    }
}