package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
import com.github.travelplannerapp.ServerApp.jsondatamodels.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.GetMapping

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

    @PostMapping("/deletetravels")
    fun deleteTravel(@RequestHeader("authorization") token: String,
                     @RequestBody travelIds: ArrayList<Int>): Response<Unit> {
        userManagement.verifyUser(token)
        val userId = userManagement.getUserId(token)
        travelManagement.deleteTravel(userId, travelIds)
        return Response(ResponseCode.OK, Unit)
    }
}
