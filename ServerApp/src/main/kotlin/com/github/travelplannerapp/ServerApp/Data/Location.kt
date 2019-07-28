package com.github.travelplannerapp.ServerApp.Data

class Location {
    private var position = Position()
    private var address=Address()


    public fun printLocation()
    {
        println("Location: ")
        position.printPosition()
        address.printAddress()
    }
}