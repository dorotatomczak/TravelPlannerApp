package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.datamodels.servermodel.CityObject
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
import com.github.travelplannerapp.ServerApp.services.searchservice.SearchService
import com.github.travelplannerapp.communication.commonmodel.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class ServerHereGoogleApiController {

    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var searchService: SearchService


    @GetMapping("here-management/objects")
    fun getObjects(
            @RequestHeader("authorization") token: String, @RequestParam("cat") category: String,
            @RequestParam("west") west: String, @RequestParam("south") south: String,
            @RequestParam("east") east: String, @RequestParam("north") north: String
    ): Response<Array<Place>> {
        userManagement.verifyUser(token)

        val items = searchService.getObjects(category, Pair(west, south), Pair(east, north))
        return Response(ResponseCode.OK, items)
    }

    @GetMapping("here-management/cities")
    fun findCities(
            @RequestHeader("authorization") token: String,
            @RequestParam("query") query: String
    ): Response<Array<CityObject>> {
        userManagement.verifyUser(token)

        try {
            val cities = searchService.getCities(query)
            return Response(ResponseCode.OK, cities)
        } catch (ex: Exception) {
            throw SearchNoItemsException(ex.localizedMessage)
        }
    }

    @GetMapping("here-management/objects/{objectId}/contacts")
    fun getContacts(
            @RequestHeader("authorization") token: String,
            @PathVariable objectId: String,
            @RequestParam("query") query: String
    ): Response<Contacts> {
        userManagement.verifyUser(token)
        val contacts = searchService.getContacts(query)
        return Response(ResponseCode.OK, contacts)
    }

    @GetMapping("here-management/objects/{objectId}")
    fun getPlace(
            @RequestHeader("authorization") token: String,
            @PathVariable objectId: String,
            @RequestParam("query") query: String
    ): Response<PlaceData> {
        userManagement.verifyUser(token)
        val place = searchService.getPlace(query)
        return Response(ResponseCode.OK, place)
    }

    @GetMapping("google-management/routes")
    fun getRoutes(
            @RequestHeader("authorization") token: String,
            @RequestParam("origin-latitude") originLat: String,
            @RequestParam("origin-longitude") originLng: String,
            @RequestParam("destination-latitude") destinationLat: String,
            @RequestParam("destination-longitude") destinationLng: String,
            @RequestParam("travel-mode") travelMode: String,
            @RequestParam("departure-time") departureTime: String
    ): Response<Routes> {
        val response = searchService.getRoutes(
                Pair(originLat, originLng),
                Pair(destinationLat, destinationLng),
                travelMode,
                departureTime
        )
        return Response(ResponseCode.OK, response)
    }
}
