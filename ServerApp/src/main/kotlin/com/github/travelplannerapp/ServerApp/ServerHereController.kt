package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.datamodels.servermodel.CityObject
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
import com.github.travelplannerapp.ServerApp.services.searchservice.SearchService
import com.github.travelplannerapp.communication.commonmodel.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class ServerHereController {

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

        try {
            val items = searchService.getObjects(category, Pair(west, south), Pair(east, north))
            return Response(ResponseCode.OK, items)
        } catch (ex: Exception) {
            throw SearchNoItemsException(ex.localizedMessage)
        }
    }

    // eg. for getting next page
    @GetMapping("here-management/objects-next-page")
    fun findObjectsGetPage(
        @RequestHeader("authorization") token: String,
        @RequestParam("request") request: String
    ): Response<SearchObjectsResponse> {
        userManagement.verifyUser(token)

        return Response(
            ResponseCode.OK,
            searchService.getPage(request)
        )
    }

    @GetMapping("here-management/cities")
    fun findCities(
        @RequestHeader("authorization") token: String,
        @RequestParam("query") query: String
    ): Response<Array<CityObject>> {
        userManagement.verifyUser(token)

        try {
            val cities = searchService.findCities(query)
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

    @GetMapping("/here-management/transport")
    fun getTransport(
        /*@RequestHeader("authorization") token: String,
        @PathVariable objectId: String,
        @RequestParam("query") query: String*/
    ): Response<String> {
        val response = searchService.getTransport(Pair("54.356246", "18.645546"),Pair("52.273956", "21.022613"), "publicTransport")
        return Response(ResponseCode.OK, "")
        //AIzaSyDf4j9NNh-YZe6iChP-ThT-CCDzBSJgcmE
    }
}
