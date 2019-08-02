package com.github.travelplannerapp.serverapp

import com.github.travelplannerapp.ServerApp.HereCharger.HereLoader
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class ServerController {
    companion object {
        const val databseActive = false
    }

    val counter = AtomicLong()

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var travelRepository: TravelRepository

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

    @GetMapping("/here")
    fun getExampleDataFromHere() {
        val connector = HereLoader()
        connector.findPlaceByText("chrysler", "40.74917", "-73.98529")
        connector.findBestWay("40.74917", "-73.98529", "45.74917",
                "-72.98529", "fastest", "car", "disabled")
    }
    @GetMapping("/travels")
    fun travels(@RequestParam(value = "name") name: String): List<String> {
        if (databseActive) {
            return travelRepository.getAllTravelsByUserName(name).map { travel -> travel.name }
        }
        return listOf("Gdańsk", "Elbląg", "Toruń", "Olsztyn", "Szczecin")
    }

    @GetMapping("/db")
    fun getUser() = Greeting(counter.incrementAndGet(), "Hello, ${userRepository.get(18)!!.name}")
}