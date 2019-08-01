package com.github.travelplannerapp.ServerApp.Data

class Address(
        val text: String,
        val house: String,
        val street: String,
        val postaCode: String,
        val district: String,
        val city: String,
        val county: String,
        val state: String,
        val stateCode: String,
        val country: String,
        val countryCode: String) {
    //TEMPORARY
    fun printAddress() {
        println("Address:")
        println(text)
        println(house)
        println(street)
        println(postaCode)
        println(district)
        println(city)
        println(county)
        println(state)
        println(stateCode)
        println(country)
        println(countryCode)
    }
}