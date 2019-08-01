package com.github.travelplannerapp.ServerApp.HereCharger

import java.net.URL
import java.net.URLConnection

class HereLoader {
    private val MY_APP_ID = "PFVgm9cqOc2OlIyiFZOO"
    private val MY_APP_TOKEN = "OrWU0j5Bb1XI5Yj-YLIhVQ"
    private val bestWayResponseFilter = "resposnse"
    private val findPlaceResponseFilter = "jsonResponse"

    fun findPlaceByText(text: String, latitude: String, longitude: String): String {
        val request = "https://places.cit.api.here.com/places/v1/" +
                "autosuggest?at=${latitude},${longitude}" +
                "&q=${text}" +
                "&app_id=${MY_APP_ID}" +
                "&app_code=${MY_APP_TOKEN}"

        val response = sendRequest(request, findPlaceResponseFilter)
        return response
    }

    fun findBestWay(first_latitude: String, first_longitude: String, second_latitude: String,
                    second_longitude: String, mode: String, transport: String, traffic: String): String {
        val request = "https://route.api.here.com/routing/7.2/calculateroute.json" +
                "?app_id=${MY_APP_ID}" +
                "&app_code=${MY_APP_TOKEN}" +
                "&waypoint0=geo!${first_latitude},${first_longitude}" +
                "&waypoint1=geo!${second_latitude},${second_longitude}" +
                "&mode=${mode};${transport};traffic:${traffic}"

        val response = sendRequest(request, bestWayResponseFilter)
        return response
    }

    private fun sendRequest(address: String, match: String): String {
        val url = URL(address)
        var response = ""
        with(url.openConnection() as URLConnection) {
            println("\nSent  request to URL : $url")
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    if (line.contains(match, ignoreCase = true))
                        response = line
                }
            }
        }
        return response
    }
}