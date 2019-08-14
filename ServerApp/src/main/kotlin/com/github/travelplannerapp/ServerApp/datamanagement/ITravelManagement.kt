package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonAddTravelAnswer
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonAddTravelRequest

interface ITravelManagement {
    fun addTravel(loginRequest: JsonAddTravelRequest): JsonAddTravelAnswer
}