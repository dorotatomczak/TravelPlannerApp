package com.github.travelplannerapp.ServerApp.Data

public class Place {

    private var id:String=""
    private var title:String="";
    private var vicinity:String="";
    private var position:Position=Position();
    private var category:String="";
    private var categoryTitle:String=""
    private var href:String="";
    private var resultType:String=""
    private var distance:Double=0.0;

    constructor ( id:String, title:String,vicinity:String,latitude:Double,longitude:Double,category:String,
                  categoryTitle:String,href:String,resultType:String,distance:Double){
        this.id=id;
        this.title=title;
        this.vicinity=vicinity;
        this.position.setLatitude(latitude);
        this.position.setLongitude(longitude);
        this.category=category;
        this.categoryTitle=categoryTitle;
        this.href=href;
        this.resultType=resultType;
        this.distance=distance;
    }
    public fun getId():String{
        return id;
    }
    public fun getTitle():String{
        return title;
    }
    public fun getDistance():Double{
        return distance;
    }
    public fun getHref():String{
        return href;
    }
    public fun getResultType():String{
        return resultType;
    }

    public fun getCategory():String{
        return category;
    }
    public fun getCategoryTitle():String{
        return categoryTitle;
    }
    public fun getVicinity():String{
        return vicinity;
    }
    public fun getPosition():Position{
        return position;
    }

    public fun printPlace(){
        println("Place: ")
        println(this.id)
        println(this.title);
        println(this.vicinity);
        println(this.position);
        println(this.position);
        println(this.category);
        println(this.categoryTitle);
        println(this.href);
        println(this.resultType);
        println(this.distance);
    }
}