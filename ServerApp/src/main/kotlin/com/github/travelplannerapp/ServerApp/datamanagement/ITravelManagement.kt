package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.communication.commonmodel.PlanElement

interface ITravelManagement {
    fun getTravels(userId: Int): MutableList<Travel>
    fun addTravel(userId: Int, travelName: String): Travel
    fun getTravel(id:Int): Travel?
    fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel?
    fun deleteTravels(userId: Int, travelIds: MutableSet<Int>)
    fun getPlanElements(travelId: Int): MutableList<PlanElement>
    fun addPlanElement(travelId: Int, planElement: PlanElement): PlanElement
    fun updatePlanElement(travelId: Int, planElement: PlanElement): PlanElement
    fun deletePlanElements(planElementIds: List<Int>)
}
