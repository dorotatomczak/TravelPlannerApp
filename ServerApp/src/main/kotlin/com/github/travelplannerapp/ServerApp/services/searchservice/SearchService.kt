package com.github.travelplannerapp.ServerApp.services.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.servermodel.CityObject
import com.github.travelplannerapp.ServerApp.datamodels.servermodel.SearchCitiesResponse
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
import com.github.travelplannerapp.communication.commonmodel.*
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

    override fun getCities(query: String): Array<CityObject> {
        val cities = hereLoader.getCities(query)

        if (cities.isEmpty()) throw SearchNoItemsException("No cities found")
        return cities
    }

    override fun getContacts(href: String): Contacts {
        return hereLoader.getContacts(href)
    }

    override fun getRoutes(
            startCoordinates: Pair<String, String>,
            destinationCoordinates: Pair<String, String>,
            transportMode: String,
            departureTime: String
    ): Routes {
        val routes = hereLoader.getRoutes(
                startCoordinates.first,
                startCoordinates.second,
                destinationCoordinates.first,
                destinationCoordinates.second,
                transportMode,
                departureTime)
        routes.routes.forEach { route ->
            run {
                route.legs[0].steps.forEach { step ->
                    run {
                        step.html_instructions = escapeHtml(step.html_instructions)
                    }
                }
            }
        }
        return routes
    }

    fun getPlace(query: String): PlaceData {
        val place = hereLoader.getPlace(query)

        if (place.name != null) place.name = escapeHtml(place.name!!)
        if (place.location != null) place.location!!.address.text = escapeHtml(place.location!!.address.text)
        if (place.extended != null && place.extended!!.openingHours != null) place.extended!!.openingHours!!.text =
                escapeHtml(place.extended!!.openingHours!!.text)

        return place
    }

    private fun escapeHtml(str: String): String {
        var resultStr = StringEscapeUtils.unescapeHtml3(str).replace("<br/>", "\n")
        resultStr = StringEscapeUtils.unescapeHtml3(resultStr).replace("<b>", "")
        resultStr = StringEscapeUtils.unescapeHtml3(resultStr).replace("</b>", "")
        resultStr = StringEscapeUtils.unescapeHtml3(resultStr).replace("<div style=\"font-size:0.9em\">", "")
        resultStr = StringEscapeUtils.unescapeHtml3(resultStr).replace("</div>", "")
        return resultStr
    }

    @Component
    @Configuration
    private inner class HereLoader {
        @Value("\${com.here.android.maps.appid}")
        private lateinit var MY_APP_HERE_ID: String

        @Value("\${com.here.android.maps.apptoken}")
        private lateinit var MY_APP_HERE_TOKEN: String

        @Value("\${com.google.android.maps.key}")
        private lateinit var MY_APP_GOOGLE_KEY: String

        private val bestWayResponseFilter = "response"
        private val jsonFilter = "jsonResponse"

        fun getPlaceByText(text: String, latitude: String, longitude: String): String {
            val request = "https://places.cit.api.here.com/places/v1/" +
                    "autosuggest?at=$latitude,$longitude" +
                    "&q=$text" +
                    "&app_id=$MY_APP_HERE_ID" +
                    "&app_code=$MY_APP_HERE_TOKEN"

            return executeHereRequest(request, jsonFilter)
        }

        fun getRoutes(
                first_latitude: String, first_longitude: String, second_latitude: String,
                second_longitude: String, transportMode: String, departure_time: String
        ): Routes {
            val request = "https://maps.googleapis.com/maps/api/directions/json" +
                    "?key=$MY_APP_GOOGLE_KEY" +
                    "&origin=$first_latitude,$first_longitude" +
                    "&destination=$second_latitude,$second_longitude" +
                    "&departure_time=$departure_time" +
                    "&mode=$transportMode" +
                    "&alternatives=true"
            val routes = parseResponse<Routes>(executeGoogleRequest(request))
            for (i in 0 until (routes.routes[0].legs[0].steps.size)) {
                println(routes.routes[0].legs[0].steps[i].html_instructions)
            }
            return routes
        }

        fun getObjects(
                west: String,
                north: String,
                east: String,
                south: String,
                category: String
        ): SearchObjectsResponse {
            val request = "https://places.cit.api.here.com/places/v1/discover/explore" +
                    "?app_id=$MY_APP_HERE_ID" +
                    "&app_code=$MY_APP_HERE_TOKEN" +
                    "&in=$west,$south,$east,$north" +
                    "&cat=$category" +
                    "&pretty"

            val response = executeHereRequest(request, jsonFilter)
            return parseResponse(response)
        }

        fun getCities(query: String): Array<CityObject> {
            val limit = 20
            val request = "https://transit.api.here.com/v3/coverage/search.json" +
                    "?app_id=$MY_APP_HERE_ID" +
                    "&app_code=$MY_APP_HERE_TOKEN" +
                    "&name=$query" +
                    "&max=$limit" +
                    "&pretty"
            val response = executeHereRequest(request, "")

            // if result is null: app doesn't show anything
            return parseResponse<SearchCitiesResponse>(response).Res.Coverage.Cities.City
        }

        fun getContacts(query: String): Contacts {
            val response = executeHereRequest(query, jsonFilter)
            return parseResponse<PlaceContactInfo>(response).contacts
        }

        fun getPlace(query: String): PlaceData {
            val response = executeHereRequest(query, jsonFilter)
            return parseResponse(response)
        }

        private inline fun <reified T> parseResponse(response: String): T {

            val responseText = response.substring(response.indexOf('{'))

            val jsonElement = JsonParser().parse(responseText)
            val gson = GsonBuilder().setPrettyPrinting().create()

            return gson.fromJson(jsonElement, T::class.java)
        }

        private fun executeHereRequest(address: String, match: String): String {
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

        private fun executeGoogleRequest(address: String): String {
            val url = URL(address)
            var response = ""
            with(url.openConnection() as URLConnection) {
                println("\nSent  request to URL : $url")
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response += line
                    }
                }
                return response
            }
        }
    }
}
