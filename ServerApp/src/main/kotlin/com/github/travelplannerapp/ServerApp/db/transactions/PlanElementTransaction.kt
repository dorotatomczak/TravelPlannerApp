package com.github.travelplannerapp.ServerApp.db.transactions

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanElementDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.db.repositories.PlanElementRepository
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PlanElementTransaction {

    @Autowired
    lateinit var placeRepository: PlaceRepository
    @Autowired
    lateinit var planElementRepository: PlanElementRepository

    fun addPlanElement(travelId: Int, planElement: PlanElement): PlanElement? {
        println("0")
        DbConnection.conn.autoCommit = false
        println("30")

        val planElementDao = PlanElementDao(travelId, planElement)
        println("340")
        val planElementId = planElementRepository.getNextId()
        println("2340")
        planElementDao.id = planElementId

        var placeDao = placeRepository.getPlaceByHereId(planElement.place.id)
        var placeId = placeDao?.id

        var queryResult: Boolean
        if (placeId != null) {
            println("40")
            planElementDao.placeId = placeId
            queryResult = planElementRepository.add(planElementDao)
        } else {
            placeId = placeRepository.getNextId()
            val rateCount = 0
            println("20")
            val rating = planElement.place.averageRating?.toDouble() ?: 0.0

            println("1")
            placeDao = PlaceDao(
                    placeId,
                    planElement.place.id,
                    planElement.place.href,
                    planElement.place.title,
                    planElement.place.vicinity,
                    planElement.place.categoryIcon,
                    rating,
                    rateCount)
            println("0")
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

    fun deletePlanElements(planElementIds: List<Int>): Boolean {
        DbConnection.conn.autoCommit = false
        var queryResult: Boolean

        for (planElementId in planElementIds) {
            queryResult = planElementRepository.delete(planElementId)
            if (!queryResult) {
                DbConnection.conn.rollback()
                DbConnection.conn.autoCommit = true
                return false
            }
        }

        DbConnection.conn.commit()
        DbConnection.conn.autoCommit = true
        return true
    }
}
