package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.datamodels.Plan
import com.github.travelplannerapp.ServerApp.db.dao.PlanDao
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.merge
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.db.repositories.PlanRepository
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.transactions.PlanTransaction
import com.github.travelplannerapp.ServerApp.db.transactions.TravelTransaction
import com.github.travelplannerapp.ServerApp.exceptions.AddPlanException
import com.github.travelplannerapp.ServerApp.exceptions.AddTravelException
import com.github.travelplannerapp.ServerApp.exceptions.DeleteTravelsException
import com.github.travelplannerapp.ServerApp.exceptions.UpdateTravelException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelManagement : ITravelManagement {

    @Autowired
    lateinit var travelTransaction: TravelTransaction
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var planTransaction: PlanTransaction

    override fun addTravel(userId: Int, travelName: String): Travel {
        val addedTravel = travelTransaction.addTravel(travelName, userId)
        if (addedTravel != null) {
            return addedTravel
        } else {
            throw AddTravelException("Error when adding travel")
        }
    }

    override fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel? {
        val travel = travelRepository.get(id)
        val travelChanges = Travel(changes)
        val updatedTravel = travelChanges merge travel!!

        val result = travelRepository.update(updatedTravel)

        return if (result) updatedTravel else throw UpdateTravelException("Error when updating travel")
    }

    override fun deleteTravels(userId: Int, travelIds: MutableSet<Int>) {
        val result = travelTransaction.deleteTravels(userId, travelIds)
        if (!result) throw  DeleteTravelsException("Error when deleting travel")
    }

    override fun addPlan(plan: Plan): Plan {
        val addedPlan = planTransaction.addPlan(plan)
        if (addedPlan != null) return addedPlan
        else throw AddPlanException("Error when adding plan")
    }
}
