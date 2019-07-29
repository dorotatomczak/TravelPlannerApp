package com.github.travelplannerapp.ServerApp.HERECharger
import com.github.travelplannerapp.ServerApp.Data.Location

public class UserLocation {
    private var l=  Location()

    public fun decodeLocation(locationString: String) {
        var readWord = ""
         var country = ""
         var alpha3 = ""
        var positionLatitude = 0.0
         var positionLongitude = 0.0
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
        l.printLocation()
    }
}
