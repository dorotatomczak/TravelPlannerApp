package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
import com.github.travelplannerapp.ServerApp.jsondatamodels.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerTravelController {

    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement

    @GetMapping("/travels")
    fun travels(@RequestHeader("authorization") token: String): Response<List<Travel>> {
        userManagement.verifyUser(token)

        val userId = userManagement.getUserId(token)
        val travels = travelRepository.getAllTravelsByUserId(userId)
        return Response(ResponseCode.OK, travels)
    }

    @PostMapping("/addtravel")
    fun addTravel(@RequestHeader("authorization") token: String,
                  @RequestBody travelName: String): Response<Travel> {
        userManagement.verifyUser(token)
        val userId = userManagement.getUserId(token)
        val newTravel = travelManagement.addTravel(userId, travelName)
        return Response(ResponseCode.OK, newTravel)
    }

    @PostMapping("/changetravelname")
    fun changeTravelName(@RequestHeader("authorization") token: String,
                         @RequestBody travel: Travel): Response<Travel> {
        userManagement.verifyUser(token)
        val updatedTravel = travelManagement.updateTravel(travel.id!!, mutableMapOf("name" to travel.name))
        return Response(ResponseCode.OK, updatedTravel)
    }

    @PostMapping("/deletetravels")
    fun deleteTravels(@RequestHeader("authorization") token: String,
                      @RequestBody travelIds: MutableSet<Int>): Response<Unit> {
        userManagement.verifyUser(token)
        val userId = userManagement.getUserId(token)
        travelManagement.deleteTravels(userId, travelIds)
        return Response(ResponseCode.OK, Unit)
    }
}
