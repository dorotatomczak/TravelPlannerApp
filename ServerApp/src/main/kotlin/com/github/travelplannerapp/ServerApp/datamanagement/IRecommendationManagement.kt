package com.github.travelplannerapp.ServerApp.datamanagement

interface IRecommendationManagement {
    fun ratePlace(placeId: Int, rating: Int): Boolean
}
