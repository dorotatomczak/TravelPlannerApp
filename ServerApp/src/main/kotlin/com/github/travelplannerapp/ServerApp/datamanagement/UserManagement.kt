package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginAnswer
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginRequest
import com.github.travelplannerapp.ServerApp.jsondatamodels.LOGIN_ANSWER
import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

@Component
class UserManagement : IUserManagement {
    @Autowired
    lateinit var userRepository: UserRepository

    override fun verifyUser(email: String, auth: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        return (user?.authToken!! == auth
                && user.expirationDate!!.after(java.sql.Date.valueOf(LocalDate.now())))
    }

    override fun authenticateUser(loginRequest: JsonLoginRequest): JsonLoginAnswer {
        val user = userRepository.getUserByEmail(loginRequest.email)
        if (user == null || user.password != loginRequest.password) {
            return JsonLoginAnswer("", LOGIN_ANSWER.ERROR)
        }
        return JsonLoginAnswer("", LOGIN_ANSWER.OK)
    }

    override fun UpdateAuthorizationToken(loginRequest: JsonLoginRequest): String {
        val claims: HashMap<String, Any?> = HashMap()

        claims["iss"] = "TravelApp Server"
        claims["sub"] = "AccessToken"
        claims["email"] = loginRequest.email
        claims["password"] = loginRequest.password
        claims["generatedTimestamp"] = LocalDate.now()

        val expiryDate = Instant.now().plusSeconds(3600 * 24)

        val randomString = generateRandomString() // TODO [Ania] change to defined somewhere key if needed

        var accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS256, randomString)
                .compact()

        accessToken = accessToken.substring(0, 40)

        userRepository.updateUserAuthByEmail(loginRequest.email, accessToken, Timestamp.from(expiryDate))

        return accessToken
    }

    override fun addUser(loginRequest: JsonLoginRequest): JsonLoginAnswer {
        val user = userRepository.getUserByEmail(loginRequest.email)
        if (user != null) {
            return JsonLoginAnswer("", LOGIN_ANSWER.ERROR)
        }

        val newUser = User(loginRequest.email, loginRequest.password)
        userRepository.add(newUser)

        return JsonLoginAnswer("", LOGIN_ANSWER.OK)
    }

    // private function
    fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return ThreadLocalRandom.current()
                .ints(10, 0, charPool.size)
                .asSequence()
                .map(charPool::get)
                .joinToString("")
    }
}