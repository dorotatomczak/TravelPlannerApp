package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanDao

interface IPlanRepository : IRepository<PlanDao>{
    fun getPlansByTravelId(travelId: Int): List<Pair<PlanDao, PlaceDao>>
}
