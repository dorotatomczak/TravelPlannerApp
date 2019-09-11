package com.github.travelplannerapp.ServerApp.services.searchservice.data

class Position(private val latitude: Double, private val longitude: Double) {
    //TEMPORARY
    fun printPosition() {
        println("Position: ")
        println("Latitude" + latitude)
        println("Longitude" + longitude)
    }
}
