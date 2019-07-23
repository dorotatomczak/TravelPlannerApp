package com.github.travelplannerapp.ServerApp.HERECharger

import java.io.File
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder

class HEREConnector {
    val MY_APP_ID = "PFVgm9cqOc2OlIyiFZOO"
    val MY_APP_TOKEN = "OrWU0j5Bb1XI5Yj-YLIhVQ"

    public fun verifyKeys(){
        val example_request="https://route.api.here.com/routing/7.2/calculateroute.json" +
                "?app_id=${MY_APP_ID}" +
                "&app_code=${MY_APP_TOKEN}" +
                "&waypoint0=geo!52.5,13.4" +
                "&waypoint1=geo!52.5,13.45" +
                "&mode=fastest;car;traffic:disabled"
        println(example_request)

        sendRequest(example_request)

    }
    private fun readResponse()//simple getting variables in pattern
    {
        val regex = """([\w\s]+) is (\d+) years old""".toRegex()
        val matchResult = regex.find("Mickey Mouse is 95 years old")
        val (name, age) = matchResult!!.destructured

        println("name: "+name+" age: "+age)

    }
    private fun sendRequest(address:String)
    {
        val url=URL(address)
        var response="";
        with(url.openConnection() as URLConnection) {

            println("\nSent  request to URL : $url;")
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                        response=line;
                }
            }
        }
        println(response)
        File("exampleRequest.txt").writeText(response)
        readResponse()
    }



    public fun printKeys()
    {
        println(MY_APP_ID)
        println(MY_APP_TOKEN)
    }
}