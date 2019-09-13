package com.github.travelplannerapp.ServerApp.services.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.CityObject
import com.github.travelplannerapp.ServerApp.datamodels.Place
import com.github.travelplannerapp.ServerApp.datamodels.SearchCitiesResponse
import com.github.travelplannerapp.ServerApp.datamodels.SearchObjectsResponse
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
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

    override fun getObjects(category: String, westSouthPoint: Pair<String, String>, eastNorthPoint: Pair<String, String>): Array<Place> {
        val places =  findObjects(
            westSouthPoint.first, westSouthPoint.second,
            eastNorthPoint.first, eastNorthPoint.second, category
        ).results.items

        if(places.isEmpty()) throw SearchNoItemsException("No places found")
        return places
    }

    override fun getPage(request: String): SearchObjectsResponse {
        return executeRequest(request)
    }

    override fun findCities(query: String): Array<CityObject> {
        val cities = getCities(query)
        if (cities.isEmpty()) throw SearchNoItemsException("No cities found")

        return cities
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

        fun findObjects(
            west: String,
            north: String,
            east: String,
            south: String,
            category: String
        ): SearchObjectsResponse {
            val request = "https://places.cit.api.here.com/places/v1/discover/explore" +
                          "?app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN" +
                          "&in=$west,$south,$east,$north" +
                          "&cat=$category" +
                          "&pretty"

            return executeRequest(request)
        }

        fun getCities(query: String): Array<CityObject> {
            val limit = 20
            val request = "https://transit.api.here.com/v3/coverage/search.json" +
                          "?app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN" +
                          "&name=$query" +
                          "&max=$limit" +
                          "&pretty"
            val response = executeRequest(request, "")
            val responseText = response.substring(response.indexOf('{'))
            val jsonElement = JsonParser().parse(responseText)
            val gson = GsonBuilder().setPrettyPrinting().create()

            // if result is null: app doesn't show anything
            return gson.fromJson(jsonElement, SearchCitiesResponse::class.java).Res.Coverage.Cities.City
        }

        fun executeRequest(request: String): SearchObjectsResponse {

            var response = executeRequest(request, jsonFilter)
            val responseText = response.substring(response.indexOf('{'))

            var jsonElement = JsonParser().parse(responseText)
            val gson = GsonBuilder().setPrettyPrinting().create()

            return gson.fromJson(jsonElement, SearchObjectsResponse::class.java)
        }

        private fun executeRequest(address: String, match: String): String {
            val url = URL(address)
            var response = ""
            with(url.openConnection() as URLConnection) {
                println("\nSent  request to URL : $url")
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        if (match == "" || line.contains(match, ignoreCase = true))
                            response = line
                    }
                }
            }
            return response
        }
    }
}
