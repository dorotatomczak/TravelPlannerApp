package com.github.travelplannerapp.ServerApp.Data

public class Place {

//    title:Chrysler Building
//    highlightedTitle:<b>Chrysler</b> Building
//    vicinity:405 Lexington Ave<br/>New York, NY 10017
//    highlightedVicinity:405 Lexington Ave<br/>New York, NY 10017
//    position: [ 40.75196,-73.97565 ]
//    category:sights-museums
//    categoryTitle:Atrakcje turystyczne i muzea
//    href:https://places.cit.api.here.com/places/v1/places/840dr5ru-08bf72d1a15f41228438fb017f83a84c;context=Zmxvdy1pZD0yNzVlMTIxZi1lZDgyLTUwMmYtYmM3NC1lZjU3ODRhOWRiN2FfMTU2NDM0NDM5OTk1MF80MjM0XzI1MTMmcmFuaz0x?app_id=PFVgm9cqOc2OlIyiFZOO&app_code=OrWU0j5Bb1XI5Yj-YLIhVQ
//    type:urn:nlp-types:place
//    resultType:place
//    id:840dr5ru-08bf72d1a15f41228438fb017f83a84c
//    distance:871

    private var title:String="";
    private var vicinity:String="";
    private var position:Location=Location();
    private var category="";
    private var categoryTitle=""
    private var href="";
    private var user_rate:Double=0.0;

    constructor (id:String,name:String,country:String,latitude:Double,longitude:Double,category:String,url:String,user_rate:Double){
        this.id=id;
        this.name=name;
        location.setCountry(country)
        location.setLatitude(latitude)
        location.setLongitude(longitude)
    }
    public fun getID():String?{
        return id;
    }
    public fun getName():String?{
        return name;
    }
    public fun getLocation():Location?{
        return location;
    }
    public fun getCategory():String?{
        return category;
    }
    public fun getIcon():String?{
        return icons_url;
    }
    public fun getRate():Double?{
        return user_rate;
    }
    public fun printPlace(){
        println("Place: ")
        println(this.id)
        println(this.name)
        location.printLocation();
        println(this.category)
        println(this.icons_url)
        println(this.user_rate)
    }
}