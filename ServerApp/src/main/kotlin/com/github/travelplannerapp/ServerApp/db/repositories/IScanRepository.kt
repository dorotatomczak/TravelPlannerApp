package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.dao.Scan

interface IScanRepository : IRepository<Scan> {
    fun getAll(userId: Int, travelId: Int): MutableSet<Scan>
}
