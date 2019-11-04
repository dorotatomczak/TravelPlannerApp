package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.merge
import com.github.travelplannerapp.ServerApp.db.repositories.PlanElementRepository
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.transactions.PlanElementTransaction
import com.github.travelplannerapp.ServerApp.db.transactions.TravelTransaction
import com.github.travelplannerapp.ServerApp.exceptions.*
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelManagement : ITravelManagement {

    @Autowired
    lateinit var scanManagement: ScanManagement
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

    override fun getTravel(id: Int): Travel? {
        return travelRepository.get(id)
    }

    override fun updateTravel(id: Int, changes: MutableMap<String, Any?>): Travel? {
        val travel = travelRepository.get(id)
        val travelChanges = Travel(changes)
        val updatedTravel = travelChanges merge travel!!

        val result = travelRepository.update(updatedTravel)

        return if (result) updatedTravel else throw UpdateTravelException("Error when updating travel")
    }

    override fun deleteTravels(userId: Int, travelIds: MutableSet<Int>) {
        for (travelId in travelIds) {
            var result = planElementRepository.deletePlanElementsByTravelId(travelId)
            if (!result) throw DeletePlanElementsException("Error when deleting plan elements")

            val scans = scanManagement.getScans(userId, travelId)
            scanManagement.deleteScans(scans)

            result = travelTransaction.deleteTravels(userId, mutableSetOf(travelId))
            if (!result) throw  DeleteTravelsException("Error when deleting travel")
        }
    }

    override fun getPlanElements(travelId: Int): MutableList<PlanElement> {
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
                    placeDao.category!!
            )
            place.averageRating = placeDao.averageRating.toString()

            val planElement = PlanElement(
                    planElementDao.id!!,
                    planElementDao.fromDateTime!!.time,
                    planElementDao.placeId!!,
                    place,
                    planElementDao.completed!!
            )
            planElements.add(planElement)
        }
        return planElements
    }

    override fun addPlanElement(travelId: Int, planElement: PlanElement): PlanElement {
        val addedPlanElement = planElementTransaction.addPlanElement(travelId, planElement)
        if (addedPlanElement != null) return addedPlanElement
        else throw AddPlanElementException("Error when adding plan element")
    }

    override fun updatePlanElement(travelId: Int, planElement: PlanElement): PlanElement {
        val updatedPlanElement = planElementTransaction.updatePlanElement(travelId, planElement)
        if (updatedPlanElement != null) return updatedPlanElement
        else throw UpdatePlanElementException("Error when updating plan element")
    }

    override fun deletePlanElements(planElementIds: List<Int>) {
        val result = planElementTransaction.deletePlanElements(planElementIds)
        if (!result) throw DeletePlanElementsException("Error when deleting plan elements")
    }
}
