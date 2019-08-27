package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.merge
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.exceptions.AuthorizationException
import com.github.travelplannerapp.ServerApp.exceptions.EmailAlreadyExistsException
import com.github.travelplannerapp.ServerApp.exceptions.WrongCredentialsException
import com.github.travelplannerapp.ServerApp.jsondatamodels.SignInRequest
import com.github.travelplannerapp.ServerApp.jsondatamodels.SignUpRequest
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

    override fun verifyUser(userId: Int, auth: String) {
        val user = userRepository.get(userId)
        if (user?.token!! != auth
                && user.expirationDate!!.before(java.sql.Date.valueOf(LocalDate.now()))){
            throw AuthorizationException("Token expired")
        }
    }

    override fun authenticateUser(request: SignInRequest): Int {
        val user = userRepository.getUserByEmail(request.email)
        if (user == null || user.password != request.password) {
            throw WrongCredentialsException("Wrong email or password")
        }
        return user.id!!
    }

    override fun updateAuthorizationToken(request: SignInRequest): String {
        val claims: HashMap<String, Any?> = HashMap()

        claims["iss"] = "TravelApp Server"
        claims["sub"] = "AccessToken"
        claims["email"] = request.email
        claims["password"] = request.password
        claims["generatedTimestamp"] = LocalDate.now()

        val expiryDate = Instant.now().plusSeconds(3600 * 24)
        val expirationDate = Timestamp.from(expiryDate)

        val randomString = generateRandomString() // TODO [Ania] change to defined somewhere key if needed

        var token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS256, randomString)
                .compact()

        // authorization token looks like:
        // commonPart.otherCommonPart.differentPart
        // database can store 40 signs of the different part
        token = token.split('.').last().substring(0, 40)

        // TODO [Magda] delegate variables names?
        val changes = mutableMapOf<String, Any?>("token" to token, "expirationDate" to expirationDate)
        val user = userRepository.getUserByEmail(request.email)
        this.updateUser(user?.id!!, changes)

        return token
    }

    override fun addUser(request: SignUpRequest) {
        val user = userRepository.getUserByEmail(request.email)
        if (user != null) throw EmailAlreadyExistsException("User with given email already exists")

        val userId = userRepository.getNextId()
        val newUser = User(userId, request.email, request.password)
        userRepository.add(newUser)
    }

    override fun updateUser(id: Int, changes: MutableMap<String, Any?>) {
        val user = userRepository.get(id)
        val userChanges = User(changes)
        val updatedUser = userChanges merge user!!
        userRepository.update(updatedUser)
    }

    private fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return ThreadLocalRandom.current()
                .ints(40, 0, charPool.size)
                .asSequence()
                .map(charPool::get)
                .joinToString("")
    }
}