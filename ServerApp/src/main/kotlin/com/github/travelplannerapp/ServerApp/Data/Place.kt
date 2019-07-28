package com.github.travelplannerapp.ServerApp.Data

public class Place {

    private var id:String="";
    private var name:String="";
    private var location:Location=Location();
    private var category="";
    private var icons_url="";
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