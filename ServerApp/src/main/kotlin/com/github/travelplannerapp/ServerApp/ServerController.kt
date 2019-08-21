package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.HereCharger.HereLoader
import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import com.github.travelplannerapp.ServerApp.jsondatamodels.*
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
class ServerController {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userTravelRepository: UserTravelRepository
    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement

    @GetMapping("/here")
    fun getExampleDataFromHere() {
        val hereLoader = HereLoader()
        hereLoader.findPlaceByText("chrysler", "40.74917", "-73.98529")
        hereLoader.findBestWay("40.74917", "-73.98529", "45.74917",
                "-72.98529", "fastest", "car", "disabled")
    }

    @PostMapping("/authorize")
    fun authorize(@RequestHeader("authorization") token: String,
                  @RequestBody userId: Int){
        userManagement.verifyUser(userId, token)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: SignInRequest): SignInResponse {
        val userId = userManagement.authenticateUser(request)
        val authToken = userManagement.updateAuthorizationToken(request)

        return SignInResponse(authToken, userId)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: SignUpRequest) {
        userManagement.addUser(request)
    }

    @GetMapping("/travels")
    fun travels(@RequestHeader("authorization") token: String,
                @RequestParam("userId") userId: Int): List<Travel> {
        userManagement.verifyUser(userId, token)
        return travelRepository.getAllTravelsByUserId(userId)
    }

    @PostMapping("/addtravel")
    fun addTravel(@RequestHeader("authorization") token: String,
                  @RequestBody request: AddTravelRequest): Travel {
        userManagement.verifyUser(request.userId, token)
        return travelManagement.addTravel(request)
    }

    // For tests
    // [Magda] quick database access functions testing
    @GetMapping("/db")
    fun getTravel(): String  {
        val email = "jan.nowak@gmail.com"
        val user = userRepository.getUserByEmail(email)
        val travel = travelRepository.getAllTravelsByUserEmail(email)[0]
        val user_travel = userTravelRepository.get(3)
        return (user!!.email + " " + user.id + "\n" +
                travel.name + " " + travel.id + "\n" +
                " user id: " + user_travel!!.userId +
                " travelid: " + user_travel.travelId)
    }
}