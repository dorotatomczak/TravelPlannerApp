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


    @PostMapping("places/{placeHereId}/rating")
    fun ratePlace(
        @RequestHeader("authorization") token: String,
        @PathVariable placeHereId: String,
        @RequestParam("rating") rating: Int
    ): Response<Unit> {
        val result = recommendationManagement.ratePlace(placeHereId, rating)

        if (result) return Response(ResponseCode.OK)
        else throw RatePlaceException("Couldn't save rating")
    }
}
