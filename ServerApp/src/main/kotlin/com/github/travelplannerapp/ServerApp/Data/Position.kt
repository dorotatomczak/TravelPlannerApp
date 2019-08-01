package com.github.travelplannerapp.ServerApp.Data

class Position(
        private val latitude: Double,
        private val longitude: Double) {
    //TEMPORARY
    fun printPosition() {
        println("Position: ")
        println("Latitude" + latitude)
        println("Longitude" + longitude)
    }
}