package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.HereCharger.HereLoader
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerTestController {

    @Autowired
    lateinit var travelRepository: TravelRepository

    @GetMapping("/here")
    fun getExampleDataFromHere() {
        val connector = HereLoader()
        connector.findPlaceByText("chrysler", "40.74917", "-73.98529")
        connector.findBestWay("40.74917", "-73.98529", "45.74917",
                              "-72.98529", "fastest", "car", "disabled")
    }

    // [Magda] quick database access functions testing
    @GetMapping("/db")
    fun getTravel(): Travel? {
        return travelRepository.get(3)
    }

    @GetMapping("/testController")
    fun testController(): List<String> {
        return listOf("Gdańsk", "Elbląg", "Toruń", "Olsztyn", "Szczecin")
    }
}