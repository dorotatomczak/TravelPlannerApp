package com.github.travelplannerapp.ServerApp.datamanagement

import com.github.travelplannerapp.ServerApp.db.dao.Scan

interface IScanManagement {
    fun addScan(userId: Int, travelId: Int, name: String): Scan
    fun getScans(userId: Int, travelId: Int): List<Scan>
    fun deleteScans(scans: List<Scan>)
}
