package com.github.travelplannerapp.ServerApp.db.transactions

import com.github.travelplannerapp.ServerApp.datamodels.Plan
import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.db.repositories.PlanRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PlanTransaction {

    @Autowired
    lateinit var placeRepository: PlaceRepository
    @Autowired
    lateinit var planRepository: PlanRepository

    fun addPlan(plan: Plan): Plan? {
        DbConnection.conn.autoCommit = false

        val planDao = PlanDao(plan)
        val planId = planRepository.getNextId()
        planDao.id = planId

        var placeDao = placeRepository.getPlaceByHereId(plan.place.id)

        var queryResult: Boolean
        if (placeDao != null) {
            queryResult = planRepository.add(planDao)
        } else {
            val placeId = placeRepository.getNextId()
            placeDao = PlaceDao(
                    placeId, plan.place.id, plan.place.href, plan.place.title, plan.place.vicinity, plan.place.category.title)
            queryResult = placeRepository.add(placeDao)
            if (queryResult) {
                planDao.placeId = placeId
                queryResult = planRepository.add(planDao)
            }
        }

        return if (queryResult) {
            DbConnection.conn.commit()
            DbConnection.conn.autoCommit = true
            plan.id = planId
            plan.placeId = placeDao.id!!
            plan
        } else {
            DbConnection.conn.rollback()
            DbConnection.conn.autoCommit = true
            null
        }
    }
}
