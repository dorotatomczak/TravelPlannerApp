package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao

interface IRecommendationManagement {
    fun ratePlace(userId: Int, placeId: Int, rating: Int): Boolean
    fun getPlaceRating(userId: Int, placeId: Int): Int?
    fun getRecommendations(userId: Int): List<PlaceDao>
}
