package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.exceptions.RatePlaceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RecommendationManagement : IRecommendationManagement {

    @Autowired
    lateinit var placeRepository: PlaceRepository


    override fun ratePlace(placeHereId: String, rating: Int): Boolean {
        val placeDao = placeRepository.getPlaceByHereId(placeHereId)
        if (placeDao != null) {
            placeDao.rating = (placeDao.rating!! * placeDao.rateCount!! + rating) / placeDao.rateCount!! + 1
            placeDao.rateCount = placeDao.rateCount!! + 1

            return placeRepository.update(placeDao)
        }

        throw RatePlaceException("Cannot add new rating. Place doesn't exist in database")
    }
}
