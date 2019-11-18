package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao

interface IPlaceRepository: IRepository<PlaceDao> {
    fun getPlaceByHereId(hereId: String): PlaceDao?
    fun getPlacesByIds(placeIds: Array<Int>): List<PlaceDao>
}
