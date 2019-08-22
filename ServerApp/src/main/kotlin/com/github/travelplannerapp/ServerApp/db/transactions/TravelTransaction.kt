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

    fun deleteTravel(user_id: Int, travel_id: Int): Boolean {
        DbConnection.conn.autoCommit = false

        var queryResult = userTravelRepository.deleteUserTravelBinding(user_id, travel_id)
        if (queryResult) {
            if(userTravelRepository.countByTravelId(travel_id) > 0){
                DbConnection.conn.commit()
                DbConnection.conn.autoCommit = true
                return true
            } else {
                queryResult = travelRepository.delete(travel_id)
                if(queryResult) {
                    DbConnection.conn.commit()
                    DbConnection.conn.autoCommit = true
                    return true
                }
            }
        }
        DbConnection.conn.rollback()
        DbConnection.conn.autoCommit = true
        return false
    }
}