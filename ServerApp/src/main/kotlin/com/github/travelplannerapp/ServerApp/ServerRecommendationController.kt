package com.github.travelplannerapp.ServerApp

import com.github.travelplannerapp.ServerApp.datamanagement.RecommendationManagement
import com.github.travelplannerapp.ServerApp.exceptions.RatePlaceException
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerRecommendationController {

    @Autowired
    lateinit var recommendationManagement: RecommendationManagement

    @PostMapping("places/{placeId}/rating")
    fun ratePlace(
        @RequestHeader("authorization") token: String,
        @PathVariable placeId: Int,
        @RequestParam("rating") rating: Int
    ): Response<Unit> {
        val result = recommendationManagement.ratePlace(placeId, rating)
        if (result) return Response(ResponseCode.OK, Unit)
        else throw RatePlaceException("Couldn't save rating")
    }
}
