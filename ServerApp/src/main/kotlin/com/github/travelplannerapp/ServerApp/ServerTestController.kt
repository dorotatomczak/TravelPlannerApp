package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.HereCharger.HereLoader
import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerTestController {

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
        val connector = HereLoader()
        connector.findPlaceByText("chrysler", "40.74917", "-73.98529")
        connector.findBestWay("40.74917", "-73.98529", "45.74917",
                              "-72.98529", "fastest", "car", "disabled")
    }

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

    @GetMapping("/testController")
    fun testController(): List<String> {
        return listOf("Gdańsk", "Elbląg", "Toruń", "Olsztyn", "Szczecin")
    }
}