package com.github.travelplannerapp.ServerApp.datamanagement

interface IScanManagement {
    fun addScan(userId: Int, travelId: Int, name: String)
}