package com.github.travelplannerapp.ServerApp.searchservice.data

class Location(private val position: Position, private val address: Address) {
    //TEMPORARY
    fun printLocation() {
        println("Location: ")
        address.printAddress()
    }
}
