package com.github.travelplannerapp.ServerApp.Data

 class Position {
    private var latitude = 0.0
    private var longitude = 0.0

    public fun getLatitude():Double?{
        return latitude;
    }
    public fun getLongitude():Double?{
        return longitude;
    }
    public fun setLatitude(x: Double){
        latitude=x;
    }
    public fun setLongitude(y: Double){
        longitude=y;
    }

    public fun printPosition()
    {
        println("Position: ")
        println("Latitude"+latitude)
        println("Longitude"+longitude)
    }
}