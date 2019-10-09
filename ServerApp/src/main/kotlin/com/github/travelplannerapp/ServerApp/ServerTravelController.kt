package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
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

    @GetMapping("users/{userId}/travels")
    fun getTravels(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int
    ): Response<List<Travel>> {
        userManagement.verifyUser(token)
        val travels = travelRepository.getAllTravelsByUserId(userId)
        return Response(ResponseCode.OK, travels)
    }

    @PostMapping("users/{userId}/travels")
    fun addTravel(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travelName: String
    ): Response<Travel> {
        userManagement.verifyUser(token)
        val newTravel = travelManagement.addTravel(userId, travelName)
        return Response(ResponseCode.OK, newTravel)
    }

    @PutMapping("users/{userId}/travels")
    fun changeTravelName(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travel: Travel
    ): Response<Travel> {
        userManagement.verifyUser(token)
        val updatedTravel = travelManagement.updateTravel(travel.id!!, mutableMapOf("name" to travel.name))
        return Response(ResponseCode.OK, updatedTravel)
    }

    @DeleteMapping("users/{userId}/travels")
    fun deleteTravels(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travelIds: MutableSet<Int>
    ): Response<Unit> {
        userManagement.verifyUser(token)
        travelManagement.deleteTravels(userId, travelIds)
        return Response(ResponseCode.OK, Unit)
    }

    @GetMapping("/users/{userId}/travels/{travelId}/plans")
    fun getPlans(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @PathVariable travelId: Int
    ): Response<List<PlanElement>> {
        userManagement.verifyUser(token)

        val planElements = travelManagement.getPlanElements(travelId)
        return Response(ResponseCode.OK, planElements)
    }

    @PostMapping("/users/{userId}/travels/{travelId}/plans")
    fun addPlan(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @PathVariable travelId: Int,
            @RequestBody planElement: PlanElement
    ): Response<PlanElement> {
        userManagement.verifyUser(token)

        val addedPlanElement = travelManagement.addPlanElement(travelId, planElement)
        return Response(ResponseCode.OK, addedPlanElement)
    }

    @DeleteMapping("users/{userId}/plans")
    fun deletePlanElements(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @RequestBody planElementIds: List<Int>
    ): Response<Unit> {
        userManagement.verifyUser(token)
        travelManagement.deletePlanElements(userId, planElementIds)
        return Response(ResponseCode.OK, Unit)
    }
}
