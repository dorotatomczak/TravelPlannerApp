package com.github.travelplannerapp.ServerApp.HERECharger

import java.net.URL
import java.net.URLConnection

class HEREConnector {

    val MY_APP_ID = "PFVgm9cqOc2OlIyiFZOO"
    val MY_APP_TOKEN = "OrWU0j5Bb1XI5Yj-YLIhVQ"

    public fun findPlaceByText(text:String,latitude:String,longitude:String):String{

        val request="https://places.cit.api.here.com/places/v1/"+
                "autosuggest?at=${latitude},${longitude}"+
                "&q=${text}"+
                "&app_id=${MY_APP_ID}"+
                "&app_code=${MY_APP_TOKEN}"

        println(request)
       return sendRequest(request,"jsonResponse")
    }

    public fun findBestWay(first_latitude:String,first_longitude:String,second_latitude:String,
                           second_longitude:String,mode:String,transport:String,traffic:String):String{

        val request="https://route.api.here.com/routing/7.2/calculateroute.json" +
                "?app_id=${MY_APP_ID}" +
                "&app_code=${MY_APP_TOKEN}" +
                "&waypoint0=geo!${first_latitude},${first_longitude}" +
                "&waypoint1=geo!${second_latitude},${second_longitude}" +
                "&mode=${mode};${transport};traffic:${traffic}"

        println(request)
        return sendRequest(request,"response")
    }

    private fun sendRequest(address:String,match:String):String
    {
        val url=URL(address)
        var response=""
        with(url.openConnection() as URLConnection) {

            println("\nSent  request to URL : $url")
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    if(line.contains(match, ignoreCase = true))
                        response=line;


                }

            }
        }
        println(response)
        return response;
    }
    public fun printKeys()
    {
        println(MY_APP_ID)
        println(MY_APP_TOKEN)
    }
}