package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanElementDao

interface IPlanElementRepository : IRepository<PlanElementDao>{
    fun getPlanElementsByTravelId(travelId: Int): List<Pair<PlanElementDao, PlaceDao>>
}
