package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.datamodels.ObjectCategory
import com.github.travelplannerapp.ServerApp.datamodels.Place
import com.github.travelplannerapp.ServerApp.datamodels.Plan
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.merge
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
    lateinit var planTransaction: PlanTransaction
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var planRepository: PlanRepository

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

    override fun getPlans(travelId: Int) : MutableList<Plan> {
        val plans = mutableListOf<Plan>()
        val plansDaoPlaceDao = planRepository.getPlansByTravelId(travelId)
        plansDaoPlaceDao.map { pair ->  {
            val planDao = pair.first
            val placeDao = pair.second
            val place = Place(
                    placeDao.hereId!!,
                    placeDao.title!!,
                    placeDao.vicinity!!,
                    emptyArray<Double>(),
                    placeDao.category!!,
                    ObjectCategory(),
                    placeDao.href!!)
            val plan = Plan(planDao.id!!,
                    planDao.locale!!,
                    planDao.fromDateTime!!.time,
                    planDao.toDateTime!!.time,
                    planDao.placeId!!,
                    place)
            plans.add(plan)
        }}
        return plans
    }

    override fun addPlan(travelId: Int, plan: Plan): Plan {
        val addedPlan = planTransaction.addPlan(travelId, plan)
        if (addedPlan != null) return addedPlan
        else throw AddPlanException("Error when adding plan")
    }
}
