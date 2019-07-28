package com.github.travelplannerapp.ServerApp.Data

class Address {

    var text:String=""
    var house:String=""
    var street:String=""
    var postaCode:String=""
    var district:String=""
    var city:String=""
    var state:String=""
    var stateCode:String=""
    var country:String="" //two times is Country!!
    var countryCode:String=""

    public fun printAddress(){
        println("Address:")
        println(text)
        println(house)
        println(street)
        println(postaCode)
        println(district)
        println(city)
        println(state)
        println(stateCode)
        println(country)
        println(countryCode)
    }

}