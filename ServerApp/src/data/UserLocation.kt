package data

class UserLocation {
    private var country=""
    private var alpha3=""
    private var positionLatitude=0.0
    private var positionLongitude=0.0

    fun decodeLocation(locationString: String)
    {
        var readWord=""
        //for (i in location.indices) {
            println(locationString)
        //}


        for(i in locationString.indices)
        {
            if(i>=34 && i<40) //charge country
            {if (locationString[i]==='"') break;
                readWord+=locationString[i]}
        }
        country=readWord

        readWord=""
        println(country)
        for(i in locationString.indices)
        {
            if(i>=48 && i<55) //charge country
            {if (locationString[i]==='"') break;
                readWord+=locationString[i]}
        }
        alpha3=readWord
        println(alpha3)
        readWord=""
        for(i in locationString.indices) {
            if (i >= 76 && i < 100) //charge country
            {
                if (locationString[i] === ',') break;
                readWord += locationString[i]
            }
        }
        positionLatitude= readWord.toDouble()
        println(positionLatitude)
        readWord=""
        for(i in locationString.indices) {
            if (i >= 106 && i < 130) //charge country
            {
                if (locationString[i] === '}') break;
                readWord += locationString[i]
            }
        }
        positionLongitude=readWord.toDouble()
        println(positionLongitude)


    }

}