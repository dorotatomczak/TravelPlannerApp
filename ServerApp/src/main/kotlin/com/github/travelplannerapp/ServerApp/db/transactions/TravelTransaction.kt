package com.github.travelplannerapp.ServerApp.db.transactions

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelTransaction {
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userTravelRepository: UserTravelRepository

    fun addTravel(travelName: String, userId: Int): Travel? {
        DbConnection.conn.autoCommit = false

        val travelId = travelRepository.getNextId()
        val travel = Travel( travelId, travelName)
        var queryResult = travelRepository.add(travel)
        if (queryResult) {
            val userTravelId = userTravelRepository.getNextId()
            queryResult = userTravelRepository.add(UserTravel(userTravelId, userId, travelId))
            if (queryResult) {
                DbConnection.conn.commit()
                DbConnection.conn.autoCommit = true
                return travel
            }
        }
        DbConnection.conn.rollback()
        DbConnection.conn.autoCommit = true
        return null
    }

    fun deleteTravels(userId: Int, travelIds: ArrayList<Int>): Boolean {
        DbConnection.conn.autoCommit = false
        var status = true
        for (travelId in travelIds) {
            var queryResult = userTravelRepository.deleteUserTravelBinding(userId, travelId)
            if (queryResult) {
                if (userTravelRepository.countByTravelId(travelId) == 0) {
                    queryResult = travelRepository.delete(travelId)
                    if (!queryResult) {
                        status = false
                        break
                    }
                }
            } else {
                status = false
                break
            }
        }

        return if (status) {
            DbConnection.conn.commit()
            DbConnection.conn.autoCommit = true
            true
        } else {
            DbConnection.conn.rollback()
            DbConnection.conn.autoCommit = true
            false
        }
    }
}
