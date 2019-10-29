package com.github.travelplannerapp.ServerApp.db.transactions

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.UserPlace
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserPlaceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class RatingTransaction {

    @Autowired
    lateinit var placeRepository: PlaceRepository

    @Autowired
    lateinit var userPlaceRepository: UserPlaceRepository

    fun ratePlace(userId: Int, placeId: Int, rating: Int): Boolean {
        DbConnection.conn.autoCommit = false

        try {
            val placeDao = placeRepository.get(placeId)
            if (placeDao != null) {
                placeDao.averageRating =
                        (placeDao.averageRating!! * placeDao.ratesCount!! + rating) / (placeDao.ratesCount!! + 1.0)
                placeDao.ratesCount = placeDao.ratesCount!! + 1

                if (placeRepository.update(placeDao)) {

                    val userPlaceId = userPlaceRepository.getNextId()
                    val userPlace = UserPlace(userPlaceId, userId, placeId, rating)

                    if (userPlaceRepository.add(userPlace)) {
                        DbConnection.conn.commit()
                        DbConnection.conn.autoCommit = true
                        return true
                    }
                }
            }
        } catch (ex: Exception) {
        }

        DbConnection.conn.rollback()
        DbConnection.conn.autoCommit = true
        return false
    }
}
