package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonAddTravelAnswer
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonAddTravelRequest

interface ITravelManagement {
    fun addTravel(addTravelRequest: JsonAddTravelRequest): JsonAddTravelAnswer

    fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel?
}