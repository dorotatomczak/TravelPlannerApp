package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.communication.commonmodel.Plan

interface ITravelManagement {
    fun addTravel(userId: Int, travelName: String): Travel
    fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel?
    fun deleteTravels(userId: Int, travelIds: MutableSet<Int>)
    fun getPlans(travelId:Int) : MutableList<Plan>
    fun addPlan(travelId: Int, plan: Plan): Plan
}
