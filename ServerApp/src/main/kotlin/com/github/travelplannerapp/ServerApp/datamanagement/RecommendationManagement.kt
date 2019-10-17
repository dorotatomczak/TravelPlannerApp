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
            placeDao.averageRating =
                (placeDao.averageRating!! * placeDao.ratesCount!! + rating) / (placeDao.ratesCount!! + 1.0)
            placeDao.ratesCount = placeDao.ratesCount!! + 1

            return placeRepository.update(placeDao)
        }

        throw RatePlaceException("Cannot add new rating. Place doesn't exist in database")
    }
}
