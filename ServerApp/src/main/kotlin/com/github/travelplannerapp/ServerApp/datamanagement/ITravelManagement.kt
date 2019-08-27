package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.jsondatamodels.AddTravelRequest

interface ITravelManagement {
    fun addTravel(request: AddTravelRequest): Travel

    fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel?
}