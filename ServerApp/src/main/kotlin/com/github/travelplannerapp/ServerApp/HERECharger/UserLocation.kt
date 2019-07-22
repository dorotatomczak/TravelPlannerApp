package com.github.travelplannerapp.ServerApp.HERECharger

public class UserLocation {
    private var country = ""
    private var alpha3 = ""
    private var positionLatitude = 0.0
    private var positionLongitude = 0.0

    public fun decodeLocation(locationString: String) {
        var readWord = ""
        println(locationString)


        for (i in locationString.indices) {
            if (i >= 34 && i < 40) //charge country
            {
                if (locationString[i] === '"') break;
                readWord += locationString[i]
            }
        }
        country = readWord

        readWord = ""
        for (i in locationString.indices) {
            if (i >= 48 && i < 55) //charge country
            {
                if (locationString[i] === '"') break;
                readWord += locationString[i]
            }
        }
        alpha3 = readWord
        readWord = ""
        for (i in locationString.indices) {
            if (i >= 76 && i < 100) //charge country
            {
                if (locationString[i] === ',') break;
                readWord += locationString[i]
            }
        }
        positionLatitude = readWord.toDouble()

        readWord = ""
        for (i in locationString.indices) {
            if (i >= 106 && i < 130) //charge country
            {
                if (locationString[i] === '}') break;
                readWord += locationString[i]
            }
        }
        positionLongitude = readWord.toDouble()

    }

    public fun printUserLocation() {

        println("User location");
        println(country)
        println(alpha3)
        println(positionLatitude)
    }
}
