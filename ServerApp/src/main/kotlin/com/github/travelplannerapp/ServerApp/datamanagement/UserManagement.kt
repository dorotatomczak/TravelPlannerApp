package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.jsondatamodels.*
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

    override fun verifyUser(userId: Int, auth: String): Boolean {
        val user = userRepository.get(userId)
        return (user?.authToken!! == auth
                && user.expirationDate!!.after(java.sql.Date.valueOf(LocalDate.now())))
    }

    override fun authenticateUser(request: SignInRequest): Int {
        val user = userRepository.getUserByEmail(request.email)
        if (user == null || user.password != request.password) {
            throw Exception("Wrong email or password")
        }
        return user.id
    }

    override fun updateAuthorizationToken(request: SignInRequest): String {
        val claims: HashMap<String, Any?> = HashMap()

        claims["iss"] = "TravelApp Server"
        claims["sub"] = "AccessToken"
        claims["email"] = request.email
        claims["password"] = request.password
        claims["generatedTimestamp"] = LocalDate.now()

        val expiryDate = Instant.now().plusSeconds(3600 * 24)

        val randomString = generateRandomString() // TODO [Ania] change to defined somewhere key if needed

        var accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS256, randomString)
                .compact()

        // authorization token looks like:
        // commonPart.otherCommonPart.differentPart
        // database can store 40 signs of the different part
        accessToken = accessToken.split('.').last().substring(0, 40)
        userRepository.updateUserAuthByEmail(request.email, accessToken, Timestamp.from(expiryDate))

        return accessToken
    }

    override fun addUser(request: SignUpRequest) {
        val user = userRepository.getUserByEmail(request.email)
        if (user != null) throw Exception("User with given email already exists")

        val newUser = User(request.email, request.password)
        userRepository.add(newUser)
    }

    // private function
    fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return ThreadLocalRandom.current()
                .ints(40, 0, charPool.size)
                .asSequence()
                .map(charPool::get)
                .joinToString("")
    }
}