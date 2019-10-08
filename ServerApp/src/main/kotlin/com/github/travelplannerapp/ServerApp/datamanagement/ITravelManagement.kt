package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.communication.commonmodel.PlanElement

interface ITravelManagement {
    fun addTravel(userId: Int, travelName: String): Travel
    fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel?
    fun deleteTravels(userId: Int, travelIds: MutableSet<Int>)
    fun getPlanElements(travelId:Int) : MutableList<PlanElement>
    fun addPlanElement(travelId: Int, planElement: PlanElement): PlanElement
}
