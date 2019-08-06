package com.github.travelplannerapp.ServerApp


import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.User
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginAnswer
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonLoginRequest
import com.github.travelplannerapp.ServerApp.jsondatamodels.LOGIN_ANSWER
import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

@RestController
class ServerController {
    companion object {
        const val databseActive = false
    }

    //val counter = AtomicLong()

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userManagement: UserManagement

    @GetMapping("/travels")
    fun travels(@RequestParam(value = "email") email: String,@RequestParam(value = "auth") auth: String): List<String> {
        if (databseActive && userManagement.verifyUser(email, auth)) {
            return travelRepository.getAllTravelsByUserEmail(email, auth).map { travel -> travel.name }
        }
        //for tests without database
        print("name: $email") // TODO [Ania} delete/move when we have database on Korlub's server
        print("auth: $auth") // TODO [Ania} delete when we have database on Korlub's server
        return listOf("Gdańsk", "Elbląg", "Toruń", "Olsztyn", "Szczecin")
    }

    /*@GetMapping("/db")
    fun getUser() = Greeting(counter.incrementAndGet(), "Hello, ${userRepository.get(18)!!.name}")*/

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: String): String {
        val loginRequest = Gson().fromJson(request, JsonLoginRequest::class.java)
        val claims : HashMap<String, Any?> = HashMap()
        claims["iss"] = "TravelApp Server"
        claims["sub"] = "AccessToken"
        claims["email"] = loginRequest.email
        claims["password"] = loginRequest.password
        claims["generatedTimestamp"] = LocalDate.now()

        val expiryDate = Instant.now().plusSeconds(3600*24)

        val randomString = generateRandomString() // TODO [Ania] change to defined somewhere key if needed

        val accessToken = Jwts.builder()
            .setClaims(claims)
            .setExpiration(Date.from(expiryDate))
            .signWith(SignatureAlgorithm.HS256, randomString)
            .compact()

        if (databseActive){
            userRepository.add(User(0, loginRequest.email, loginRequest.password,
                accessToken,  Timestamp.from(expiryDate)))
        }
        val jsonLoginAnswer = JsonLoginAnswer(accessToken, LOGIN_ANSWER.OK)
        return Gson().toJson(jsonLoginAnswer)
    }


    fun generateRandomString():String{
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return ThreadLocalRandom.current()
            .ints(10, 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }
}