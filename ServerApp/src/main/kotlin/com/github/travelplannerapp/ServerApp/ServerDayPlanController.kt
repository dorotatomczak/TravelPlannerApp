package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.datamodels.Plan
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
import com.github.travelplannerapp.ServerApp.datamodels.Response
import com.github.travelplannerapp.ServerApp.db.repositories.PlanRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerDayPlanController {

    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement
    @Autowired
    lateinit var planRepository: PlanRepository

    @GetMapping("/travels/{travelId}/plans")
    fun getPlans(
            @RequestHeader("authorization") token: String,
            @PathVariable travelId: Int
    ): Response<List<Plan>> {
        userManagement.verifyUser(token)

        val plans = planRepository.getPlansByTravelId(travelId)
        return Response(ResponseCode.OK, plans)
    }

    @PostMapping("/travels/{travelId}/plans")
    fun addPlan(
            @RequestHeader("authorization") token: String,
            @PathVariable travelId: Int,
            @RequestBody plan: Plan
    ): Response<Plan> {
        userManagement.verifyUser(token)

        val addedPlan = travelManagement.addPlan(travelId, plan)
        return Response(ResponseCode.OK, addedPlan)
    }
}
