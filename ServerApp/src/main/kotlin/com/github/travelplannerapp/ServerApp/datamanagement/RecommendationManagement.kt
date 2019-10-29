package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.repositories.UserPlaceRepository
import com.github.travelplannerapp.ServerApp.db.transactions.RatingTransaction
import com.github.travelplannerapp.ServerApp.exceptions.RatePlaceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RecommendationManagement : IRecommendationManagement {

    @Autowired
    lateinit var ratingTransaction: RatingTransaction

    @Autowired
    lateinit var userPlaceRepository: UserPlaceRepository

    override fun ratePlace(userId: Int, placeId: Int, rating: Int): Boolean {
        val placeRated = ratingTransaction.ratePlace(userId, placeId, rating)
        if (placeRated) return true
        else throw RatePlaceException("Cannot add new rating.")
    }

    override fun getPlaceRating(userId: Int, placeId: Int): Int? {
        return userPlaceRepository.getUserPlaceByUserIdAndPlaceId(userId, placeId)?.rating
    }
}
