package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.merge
import com.github.travelplannerapp.ServerApp.db.repositories.PlanElementRepository
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.transactions.PlanElementTransaction
import com.github.travelplannerapp.ServerApp.db.transactions.TravelTransaction
import com.github.travelplannerapp.ServerApp.exceptions.AddPlanElementException
import com.github.travelplannerapp.ServerApp.exceptions.AddTravelException
import com.github.travelplannerapp.ServerApp.exceptions.DeleteTravelsException
import com.github.travelplannerapp.ServerApp.exceptions.UpdateTravelException
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelManagement : ITravelManagement {

    @Autowired
    lateinit var travelTransaction: TravelTransaction
    @Autowired
    lateinit var planElementTransaction: PlanElementTransaction
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var planElementRepository: PlanElementRepository

    override fun getTravels(userId: Int): MutableList<Travel> {
        return travelRepository.getAllTravelsByUserId(userId)
    }

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

    override fun getPlanElements(travelId: Int) : MutableList<PlanElement> {
        val planElements = mutableListOf<PlanElement>()
        val planElementsDaoPlaceDao = planElementRepository.getPlanElementsByTravelId(travelId)
        planElementsDaoPlaceDao.forEach { pair ->
            val planElementDao = pair.first
            val placeDao = pair.second
            val place = Place(
                    placeDao.hereId!!,
                    placeDao.title!!,
                    placeDao.vicinity!!,
                    emptyArray(),
                    placeDao.href!!,
                    placeDao.category!!)
            val planElement = PlanElement(planElementDao.id!!,
                    planElementDao.locale!!,
                    planElementDao.fromDateTime!!.time,
                    planElementDao.toDateTime!!.time,
                    planElementDao.placeId!!,
                    place)
            planElements.add(planElement)
        }
        return planElements
    }

    override fun addPlanElement(travelId: Int, planElement: PlanElement): PlanElement {
        val addedPlanElement = planElementTransaction.addPlanElement(travelId, planElement)
        if (addedPlanElement != null) return addedPlanElement
        else throw AddPlanElementException("Error when adding plan element")
    }
}
