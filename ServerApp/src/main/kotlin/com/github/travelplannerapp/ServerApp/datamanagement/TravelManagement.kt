package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.merge
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
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
    @Autowired
    lateinit var travelRepository: TravelRepository

    override fun addTravel(addTravelRequest: JsonAddTravelRequest): JsonAddTravelAnswer {
        return if (travelTransaction.addTravel(addTravelRequest.travelName, addTravelRequest.userId))
            JsonAddTravelAnswer(ADD_TRAVEL_RESULT.OK)
        else JsonAddTravelAnswer(ADD_TRAVEL_RESULT.ERROR)
    }

    override fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel? {
        val travel = travelRepository.get(id)
        val travelChanges = Travel(changes)
        val updatedTravel = travelChanges merge travel!!

        return if (travelRepository.update(updatedTravel)) updatedTravel else null
    }
}