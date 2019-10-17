package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import com.github.travelplannerapp.ServerApp.db.dao.PlanElementDao
import com.github.travelplannerapp.communication.commonmodel.PlanElement

interface IPlanElementRepository : IRepository<PlanElementDao>{
    fun getPlanElementsByTravelId(travelId: Int): List<Pair<PlanElementDao, PlaceDao>>
    fun getPlanElementById(travelId: Int, planElement: PlanElement): PlanElementDao
}
