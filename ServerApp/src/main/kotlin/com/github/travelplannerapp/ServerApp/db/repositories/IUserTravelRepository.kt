package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.UserTravel

interface IUserTravelRepository : IRepository<UserTravel> {
    fun deleteUserTravelBinding(userId: Int, travelId: Int): Boolean
    fun countByTravelId(travelId: Int): Int
    fun doesUserTravelAccessExist(travelId: Int, userId: Int): Boolean
}
