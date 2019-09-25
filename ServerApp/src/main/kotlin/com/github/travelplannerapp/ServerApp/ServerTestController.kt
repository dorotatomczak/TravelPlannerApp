package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import com.github.travelplannerapp.ServerApp.services.searchservice.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerTestController {

    // todo [Ania] delete when repos are not needed anymore
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userTravelRepository: UserTravelRepository
    @Autowired
    lateinit var searchService: SearchService


    @GetMapping("/here")
    fun getExampleDataFromHere() {
        searchService.getExampleDataFromHere()
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
