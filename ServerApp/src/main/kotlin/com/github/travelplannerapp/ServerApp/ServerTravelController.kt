package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.exceptions.AddTravelException
import com.github.travelplannerapp.ServerApp.exceptions.ApiException
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
        return Response(200, travels)
    }

    @PostMapping("/addtravel")
    fun addTravel(@RequestHeader("authorization") token: String,
                  @RequestBody request: AddTravelRequest): Response<Travel> {
        userManagement.verifyUser(token)
        val newTravel = travelManagement.addTravel(request)
        return Response(200, newTravel)
    }

    @ExceptionHandler(AddTravelException::class)
    fun handleApiExceptions(exception: ApiException): Response<Any> {
        return Response(exception.code, null)
    }

    @ExceptionHandler(Exception::class)
    fun handlePredefinedExceptions(exception: Exception): Response<Any> {
        return Response(999, null)
    }
}