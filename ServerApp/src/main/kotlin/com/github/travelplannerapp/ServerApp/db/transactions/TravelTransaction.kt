package com.github.travelplannerapp.ServerApp.db.transactions

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserTravelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TravelTransaction {
    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var userTravelRepository: UserTravelRepository

    fun addTravel(travelName: String, userId: Int): Boolean {
        DbConnection.conn.autoCommit = false

        //TODO [Magda] app should store user id no need to look for it, it will be passed instead of email
        val travelId = getNextTravelId()
        val queryResult = travelRepository.add(Travel(travelName, travelId))
        if (queryResult) {
            userTravelRepository.add(UserTravel(userId,travelId))
            DbConnection.conn.autoCommit = false
        } else {
            DbConnection.conn.rollback()
            DbConnection.conn.autoCommit = true
            return false
        }
        DbConnection.conn.commit()

        DbConnection.conn.autoCommit = true
        return true
    }

    private fun getNextTravelId(): Int {
        return travelRepository.getNextId()
    }
}