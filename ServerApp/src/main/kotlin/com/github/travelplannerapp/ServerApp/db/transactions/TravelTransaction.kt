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

    lateinit var travel: Travel
    lateinit var userTravel: UserTravel

    fun addTravel(travelName: String, userId: Int): Travel? {
        DbConnection.conn.autoCommit = false

        val travelId = travelRepository.getNextId()
        travel = Travel(travelId, travelName)
        var queryResult = travelRepository.add(travel)
        if (queryResult) {
            val userTravelId = userTravelRepository.getNextId()
            userTravel = UserTravel(userTravelId, userId, travelId)
            queryResult = userTravelRepository.add(userTravel)
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

    fun deleteTravels(userId: Int, travelIds: MutableSet<Int>): Boolean {
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

    fun shareTravel(travelId: Int, choseFriendId: Int): Boolean {
        val userTravelId = userTravelRepository.getNextId()
        return userTravelRepository.add(UserTravel(userTravelId, choseFriendId, travelId))
    }
}
