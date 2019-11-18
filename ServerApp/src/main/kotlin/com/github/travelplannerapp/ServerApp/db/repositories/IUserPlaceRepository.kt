package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.UserPlace

interface IUserPlaceRepository {
    fun getUserPlaceByUserIdAndPlaceId(userId: Int, placeId: Int): UserPlace?
}
