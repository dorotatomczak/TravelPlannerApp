package com.github.travelplannerapp.ServerApp.datamanagement

interface IRecommendationManagement {
    fun ratePlace(placeHereId: String, rating: Int): Boolean
}
