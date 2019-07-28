package com.github.travelplannerapp.ServerApp.Data

class Location {
    private var latitude = 0.0
    private var longitude = 0.0
    private var country=""

    public fun getLatitude():Double?{
        return latitude;
    }
    public fun getLongitude():Double?{
        return longitude;
    }
    public fun getCountry():String?{
        return country;
    }
    public fun setLatitude(x: Double){
        latitude=x;
    }
    public fun setLongitude(y: Double){
        longitude=y;
    }
    public fun setCountry(name:String){
        country=name;
    }
    public fun printLocation()
    {
        println("Location: ")
        println(country)
        println(latitude)
        println(longitude)
    }
}