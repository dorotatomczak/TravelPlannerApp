package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.transactions.TravelTransaction
import com.github.travelplannerapp.ServerApp.jsondatamodels.ADD_TRAVEL_RESULT
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonAddTravelAnswer
import com.github.travelplannerapp.ServerApp.jsondatamodels.JsonAddTravelRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelManagement : ITravelManagement {

    @Autowired
    lateinit var travelTransaction: TravelTransaction

    override fun addTravel(addTravelRequest: JsonAddTravelRequest): JsonAddTravelAnswer {
        return if (travelTransaction.addTravel(addTravelRequest.travelName, addTravelRequest.email))
            JsonAddTravelAnswer(ADD_TRAVEL_RESULT.OK)
        else JsonAddTravelAnswer(ADD_TRAVEL_RESULT.ERROR)
    }
}