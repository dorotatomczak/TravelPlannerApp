package com.github.travelplannerapp.ServerApp.Data

class Location(private val position: Position, private val address: Address) {
    //TEMPORARY
    fun printLocation() {
        println("Location: ")
        position.printPosition()
        address.printAddress()
    }
}