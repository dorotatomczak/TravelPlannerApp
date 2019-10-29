package com.github.travelplannerapp.ServerApp.datamanagement

interface IRecommendationManagement {
    fun ratePlace(userId: Int, placeId: Int, rating: Int): Boolean
    fun getPlaceRating(userId: Int, placeId: Int): Int?
}
