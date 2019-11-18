package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.repositories.PlaceRepository
import com.github.travelplannerapp.ServerApp.db.repositories.UserPlaceRepository
import com.github.travelplannerapp.ServerApp.db.transactions.RatingTransaction
import com.github.travelplannerapp.ServerApp.exceptions.RatePlaceException
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RecommendationManagement : IRecommendationManagement {

    @Autowired
    lateinit var ratingTransaction: RatingTransaction

    @Autowired
    lateinit var userPlaceRepository: UserPlaceRepository

    @Autowired
    lateinit var placeRepository: PlaceRepository

    override fun ratePlace(userId: Int, placeId: Int, rating: Int): Boolean {
        val placeRated = ratingTransaction.ratePlace(userId, placeId, rating)
        if (placeRated) return true
        else throw RatePlaceException("Cannot add new rating.")
    }

    override fun getPlaceRating(userId: Int, placeId: Int): Int? {
        return userPlaceRepository.getUserPlaceByUserIdAndPlaceId(userId, placeId)?.rating
    }

    override fun getRecommendations(userId: Int): List<PlaceDao> {
        val dataModel = PostgreSQLJDBCDataModel(
                DbConnection.dataSource,
                "app_user_place",
                "app_user_id",
                "place_id",
                "rating",
                null)

        val similarity = PearsonCorrelationSimilarity(dataModel)
        val neighbor = NearestNUserNeighborhood(2, similarity, dataModel)
        val recommender = GenericUserBasedRecommender(dataModel, neighbor, similarity)
        val recommendations = recommender.recommend(userId.toLong(), 3)

        val recommendedPlacesIds = recommendations.map { it.itemID.toInt() }.toTypedArray()
        return placeRepository.getPlacesByIds(recommendedPlacesIds)
    }
}
