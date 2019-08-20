package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.TravelManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import com.github.travelplannerapp.ServerApp.db.repositories.TravelRepository
import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode
<<<<<<< HEAD
import com.github.travelplannerapp.ServerApp.jsondatamodels.Response
=======
import com.github.travelplannerapp.ServerApp.datamodels.*
import com.github.travelplannerapp.ServerApp.searchservice.SearchService
>>>>>>> #40: add server-side finding objects by category
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

    @GetMapping("/travels")
    fun travels(@RequestHeader("authorization") token: String): Response<List<Travel>> {
        userManagement.verifyUser(token)

        val userId = userManagement.getUserId(token)
        val travels = travelRepository.getAllTravelsByUserId(userId)
        return Response(ResponseCode.OK, travels)
    }

    @PostMapping("/addtravel")
    fun addTravel(@RequestHeader("authorization") token: String,
                  @RequestBody travelName: String): Response<Travel> {
        userManagement.verifyUser(token)
        val userId = userManagement.getUserId(token)
        val newTravel = travelManagement.addTravel(userId, travelName)
        return Response(ResponseCode.OK, newTravel)
    }

    @PostMapping("/deletetravels")
    fun deleteTravels(@RequestHeader("authorization") token: String,
                     @RequestBody travelIds: List<Int>): Response<Unit> {
        userManagement.verifyUser(token)
        val userId = userManagement.getUserId(token)
        travelManagement.deleteTravels(userId, travelIds)
        return Response(ResponseCode.OK, Unit)

    @GetMapping("/findObjects")
    fun findObjects(@RequestHeader("authorization") token: String, @RequestParam("west") west: String, @RequestParam("south") south: String,
                    @RequestParam("east") east: String, @RequestParam("north") north: String): Response<SearchResponse> {
        return Response(
            ResponseCode.OK,
            // if you want to test via browser
            //searchService.getObjects("eat-drink", Pair("18.516918", "54.350646"), Pair("18.776903", "54.382190"))
            searchService.getObjects("eat-drink", Pair(west, south), Pair(east, north))
        )
    }

    // eg. for getting next page
    @GetMapping("/findObjectsGetPage")
    fun findObjectsGetPage(/*@RequestHeader("authorization") token: String,*/
        @RequestParam("request") request: String): Response<SearchResponse> {
        return Response(
            ResponseCode.OK,
            searchService.getPage(request)
        )
    }
}
