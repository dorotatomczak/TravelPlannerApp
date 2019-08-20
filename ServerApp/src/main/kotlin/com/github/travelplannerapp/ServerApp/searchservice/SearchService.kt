package com.github.travelplannerapp.ServerApp.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.SearchResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.springframework.stereotype.Component
import java.net.URL
import java.net.URLConnection

@Component
class SearchService : ISearchService {

    // test function
    override fun getExampleDataFromHere() {
        print(findPlaceByText("chrysler", "40.74917", "-73.98529"))
        print(
            findBestWay(
                "40.74917", "-73.98529", "45.74917",
                "-72.98529", "fastest", "car", "disabled"
            )
        )
    }

    override fun getObjects(category: String, westSouthPoint: Pair<String, String>, eastNorthPoint: Pair<String, String>) : SearchResponse {
        return findObjects(  westSouthPoint.first, westSouthPoint.second,
                             eastNorthPoint.first, eastNorthPoint.second, category)
    }

    override fun getPage(request: String) : SearchResponse{
        return executeRequest(request)
    }



    companion object HereLoader {
        private val MY_APP_ID = "PFVgm9cqOc2OlIyiFZOO"
        private val MY_APP_TOKEN = "OrWU0j5Bb1XI5Yj-YLIhVQ"
        private val bestWayResponseFilter = "response"
        private val jsonFilter = "jsonResponse"

        fun findPlaceByText(text: String, latitude: String, longitude: String): String {
            val request = "https://places.cit.api.here.com/places/v1/" +
                          "autosuggest?at=$latitude,$longitude" +
                          "&q=$text" +
                          "&app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN"

            return executeRequest(request, jsonFilter)
        }

        fun findBestWay(
            first_latitude: String, first_longitude: String, second_latitude: String,
            second_longitude: String, mode: String, transport: String, traffic: String
        ): String {
            val request = "https://route.api.here.com/routing/7.2/calculateroute.json" +
                          "?app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN" +
                          "&waypoint0=geo!$first_latitude,$first_longitude" +
                          "&waypoint1=geo!$second_latitude,$second_longitude" +
                          "&mode=$mode;$transport;traffic:$traffic"


            return executeRequest(request, bestWayResponseFilter)
        }

        fun findObjects(west: String, south: String, east: String, north: String, category: String): SearchResponse {
            val request = "https://places.cit.api.here.com/places/v1/discover/explore" +
                          "?app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN" +
                          "&in=$west,$south,$east,$north" +
                          "&cat=$category" +
                          "&pretty"

           return executeRequest(request)
        }

        fun executeRequest(request: String): SearchResponse {

            var response = executeRequest(request, jsonFilter)
            val responseText = response.substring(response.indexOf('{'))

            var jsonElement = JsonParser().parse(responseText)
            val gson = GsonBuilder().setPrettyPrinting().create()

            return gson.fromJson(jsonElement, SearchResponse::class.java)
        }

        private fun executeRequest(address: String, match: String): String {
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
}
