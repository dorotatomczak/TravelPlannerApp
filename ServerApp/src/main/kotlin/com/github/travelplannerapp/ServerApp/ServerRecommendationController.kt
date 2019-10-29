package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.RecommendationManagement
import com.github.travelplannerapp.ServerApp.datamanagement.UserManagement
import com.github.travelplannerapp.ServerApp.exceptions.RatePlaceException
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerRecommendationController {

    @Autowired
    lateinit var recommendationManagement: RecommendationManagement

    @Autowired
    lateinit var userManagement: UserManagement

    @PostMapping("users/{userId}/places/{placeId}/rating")
    fun ratePlace(
        @RequestHeader("authorization") token: String,
        @PathVariable userId: Int,
        @PathVariable placeId: Int,
        @RequestParam("rating") rating: Int
    ): Response<Unit> {
        userManagement.verifyUser(token)
        val result = recommendationManagement.ratePlace(userId, placeId, rating)
        if (result) return Response(ResponseCode.OK, Unit)
        else throw RatePlaceException("Couldn't add rating for userId=$userId and placeId=$placeId")
    }

    @GetMapping("users/{userId}/places/{placeId}/rating")
    fun getPlaceRating(
            @RequestHeader("authorization") token: String,
            @PathVariable("userId") userId: Int,
            @PathVariable("placeId") placeId: Int): Response<Int> {
        userManagement.verifyUser(token)
        val rating = recommendationManagement.getPlaceRating(userId, placeId)
        return Response(ResponseCode.OK, rating ?: 0)
    }
}
