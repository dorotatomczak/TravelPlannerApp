package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.datamodels.*
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
import com.github.travelplannerapp.ServerApp.exceptions.SearchNoItemsException
import com.github.travelplannerapp.ServerApp.services.searchservice.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerTravelController {

    @Autowired
    lateinit var travelRepository: TravelRepository
    @Autowired
    lateinit var userManagement: UserManagement
    @Autowired
    lateinit var travelManagement: TravelManagement
    @Autowired
    lateinit var searchService: SearchService

    @GetMapping("users/{userId}/travels")
    fun getTravels(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int
    ): Response<List<Travel>> {
        userManagement.verifyUser(token)
        val travels = travelRepository.getAllTravelsByUserId(userId)
        return Response(ResponseCode.OK, travels)
    }

    @PostMapping("users/{userId}/travels")
    fun addTravel(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travelName: String
    ): Response<Travel> {
        userManagement.verifyUser(token)
        val newTravel = travelManagement.addTravel(userId, travelName)
        return Response(ResponseCode.OK, newTravel)
    }

    @PutMapping("users/{userId}/travels")
    fun changeTravelName(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travel: Travel
    ): Response<Travel> {
        userManagement.verifyUser(token)
        val updatedTravel = travelManagement.updateTravel(travel.id!!, mutableMapOf("name" to travel.name))
        return Response(ResponseCode.OK, updatedTravel)
    }

    @DeleteMapping("users/{userId}/travels")
    fun deleteTravels(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @RequestBody travelIds: MutableSet<Int>
    ): Response<Unit> {
        userManagement.verifyUser(token)
        travelManagement.deleteTravels(userId, travelIds)
        return Response(ResponseCode.OK, Unit)
    }

    @GetMapping("/users/{userId}/travels/{travelId}/plans")
    fun getPlans(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @PathVariable travelId: Int
    ): Response<List<Plan>> {
        userManagement.verifyUser(token)

        val plans = travelManagement.getPlans(travelId)
        return Response(ResponseCode.OK, plans)
    }

    @PostMapping("/users/{userId}/travels/{travelId}/plans")
    fun addPlan(
            @RequestHeader("authorization") token: String,
            @PathVariable userId: Int,
            @PathVariable travelId: Int,
            @RequestBody plan: Plan
    ): Response<Plan> {
        userManagement.verifyUser(token)

        val addedPlan = travelManagement.addPlan(travelId, plan)
        return Response(ResponseCode.OK, addedPlan)
    }

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

    @GetMapping("/here-management/objects/{objectId}/contacts")
    fun getContacts(@RequestHeader("authorization") token: String,
                    @PathVariable objectId: String,
                    @RequestParam("query") query: String): Response<Contacts> {
        userManagement.verifyUser(token)
        val contacts = searchService.getContacts(query)
        return Response(ResponseCode.OK, contacts)
    }
}
