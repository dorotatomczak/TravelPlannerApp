package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.Travel

interface ITravelRepository : IRepository<Travel> {
    fun getAllTravelsByUserId(id: Int): MutableList<Travel>

    fun getAllTravelsByUserEmail(email: String): MutableList<Travel>
}