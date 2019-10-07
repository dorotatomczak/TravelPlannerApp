package com.github.travelplannerapp.ServerApp.services.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.servermodel.CityObject
import com.github.travelplannerapp.ServerApp.datamodels.servermodel.SearchCitiesResponse
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
import com.github.travelplannerapp.communication.commonmodel.Contacts
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlaceInfo
import com.github.travelplannerapp.communication.commonmodel.SearchObjectsResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.apache.commons.lang3.StringEscapeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.net.URL
import java.net.URLConnection

@Component
class SearchService : ISearchService {
    @Autowired
    private lateinit var hereLoader: HereLoader

    // test function
    override fun getExampleDataFromHere() {
        print(hereLoader.getPlaceByText("chrysler", "40.74917", "-73.98529"))
        print(
            hereLoader.getTransport(
                "40.74917", "-73.98529", "45.74917",
                "-72.98529", "car"
            )
        )
    }

    override fun getObjects(
        category: String,
        westSouthPoint: Pair<String, String>,
        eastNorthPoint: Pair<String, String>
    ): Array<Place> {
        val places = hereLoader.getObjects(
            westSouthPoint.first, westSouthPoint.second,
            eastNorthPoint.first, eastNorthPoint.second, category
        ).results.items

        if (places.isEmpty()) throw SearchNoItemsException("No places found")

        for (place in places) {
            place.title = escapeHtml(place.title)
            place.vicinity = escapeHtml(place.vicinity)
            if (place.openingHours != null) {
                place.openingHours.text = escapeHtml(place.openingHours.text)
            }
        }

        return places
    }

    override fun getPage(request: String): SearchObjectsResponse {
        return hereLoader.getPage(request)
    }

    override fun findCities(query: String): Array<CityObject> {
        val cities = hereLoader.getCities(query)

        if (cities.isEmpty()) throw SearchNoItemsException("No cities found")
        return cities
    }

    override fun getContacts(href: String): Contacts {
        return hereLoader.getContacts(href)
    }

    override fun getTransport(
        startCoordinates: Pair<String, String>,
        destinationCoordinates: Pair<String, String>,
        transportMode: String
    ): String {
        return hereLoader.getTransport(
            startCoordinates.first,
            startCoordinates.second,
            destinationCoordinates.first,
            destinationCoordinates.second,
            transportMode
        )
    }

    private fun escapeHtml(str: String): String {
        return StringEscapeUtils.unescapeHtml3(str).replace("<br/>", "\n")
    }

    @Component
    @Configuration
    private inner class HereLoader {
        @Value("\${com.here.android.maps.appid}")
        private lateinit var MY_APP_ID: String

        @Value("\${com.here.android.maps.apptoken}")
        private lateinit var MY_APP_TOKEN: String

        private val bestWayResponseFilter = "response"
        private val jsonFilter = "jsonResponse"

        fun getPlaceByText(text: String, latitude: String, longitude: String): String {
            val request = "https://places.cit.api.here.com/places/v1/" +
                          "autosuggest?at=$latitude,$longitude" +
                          "&q=$text" +
                          "&app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN"

            return executeRequest(request, jsonFilter)
        }

        fun getTransport(
            first_latitude: String, first_longitude: String, second_latitude: String,
            second_longitude: String, transportMode: String
        ): String {
            val request = "https://route.api.here.com/routing/7.2/calculateroute.json" +
                          "?app_id=$MY_APP_ID" +
                          "&app_code=$MY_APP_TOKEN" +
                          "&waypoint0=geo!$first_latitude,$first_longitude" +
                          "&waypoint1=geo!$second_latitude,$second_longitude" +
                          "&departure=2019-10-05T11:50:00" +
                          "&mode=fastest;$transportMode" +
                          "&pretty"


            return executeRequest(request, bestWayResponseFilter)
        }

        fun getObjects(
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

            val response = executeRequest(request, jsonFilter)
            return parseResponse(response)
        }

        fun getPage(request: String): SearchObjectsResponse {
            val response = executeRequest(request, "")

            // if result is null: app doesn't show anything
            return parseResponse(response)
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

            // if result is null: app doesn't show anything
            return parseResponse<SearchCitiesResponse>(response).Res.Coverage.Cities.City
        }

        fun getContacts(query: String): Contacts {
            val response = executeRequest(query, jsonFilter)
            return parseResponse<PlaceInfo>(response).contacts
        }

        private inline fun <reified T> parseResponse(response: String): T {

            val responseText = response.substring(response.indexOf('{'))

            val jsonElement = JsonParser().parse(responseText)
            val gson = GsonBuilder().setPrettyPrinting().create()

            return gson.fromJson(jsonElement, T::class.java)
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
