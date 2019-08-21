package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.transactions.TravelTransaction
import com.github.travelplannerapp.ServerApp.jsondatamodels.AddTravelRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelManagement : ITravelManagement {

    @Autowired
    lateinit var travelTransaction: TravelTransaction

    override fun addTravel(request: AddTravelRequest): Travel {
        val addedTravel = travelTransaction.addTravel(request.travelName, request.userId)
        if (addedTravel != null) {
            return addedTravel
        } else {
            throw Exception("Error when adding travel")
        }

    }
}