package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import com.github.travelplannerapp.ServerApp.jsondatamodels.*
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerTravelController {

    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userTravelRepository: UserTravelRepository
    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement

    @GetMapping("/travels")
    fun travels(@RequestParam(value = "userId") userId: Int, @RequestParam(value = "auth") auth: String): List<String> {
        if (userManagement.verifyUser(userId, auth)) {
            return travelRepository.getAllTravelsByUserId(userId).map { travel -> travel.name }
        }
        return listOf("Gdańsk", "Elbląg", "Toruń", "Olsztyn", "Szczecin")
    }

    @PostMapping("/addtravel")
    fun addTravel(@RequestBody request: String): String {
        val addTravelRequest = Gson().fromJson(request, JsonAddTravelRequest::class.java)
        if (userManagement.verifyUser(addTravelRequest.userId, addTravelRequest.auth)) {
            val jsonAddTravelAnswer = travelManagement.addTravel(addTravelRequest)

            return Gson().toJson(jsonAddTravelAnswer)
        }
        return  Gson().toJson(JsonAddTravelAnswer(ADD_TRAVEL_RESULT.ERROR))
    }
}