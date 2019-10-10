package com.github.travelplannerapp.ServerApp.db.transactions

import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.PlanElement
import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanElementDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.db.repositories.PlanElementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PlanElementTransaction {

    @Autowired
    lateinit var placeRepository: PlaceRepository
    @Autowired
    lateinit var planElementRepository: PlanElementRepository

    fun addPlanElement(travelId: Int, planElement: PlanElement): PlanElement? {
        DbConnection.conn.autoCommit = false

        val planElementDao = PlanElementDao(travelId, planElement)
        val planElementId = planElementRepository.getNextId()
        planElementDao.id = planElementId

        var placeDao = placeRepository.getPlaceByHereId(planElement.place.id)
        var placeId = placeDao?.id

        var queryResult: Boolean
        if (placeId != null) {
            planElementDao.placeId = placeId
            queryResult = planElementRepository.add(planElementDao)
        } else {
            placeId = placeRepository.getNextId()
            placeDao = PlaceDao(
                    placeId,
                    planElement.place.id,
                    planElement.place.href,
                    planElement.place.title,
                    planElement.place.vicinity,
                    planElement.place.categoryIcon)
            queryResult = placeRepository.add(placeDao)
            if (queryResult) {
                planElementDao.placeId = placeId
                queryResult = planElementRepository.add(planElementDao)
            }
        }

        return if (queryResult) {
            DbConnection.conn.commit()
            DbConnection.conn.autoCommit = true
            planElement.id = planElementId
            planElement.placeId = placeId
            planElement
        } else {
            DbConnection.conn.rollback()
            DbConnection.conn.autoCommit = true
            null
        }
    }
}
