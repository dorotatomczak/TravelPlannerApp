package com.github.travelplannerapp.communication.commonmodel

import java.io.Serializable

data class PlanElement(
        var id: Int,
        var fromDateTimeMs: Long,
        var placeId: Int,
        var place: Place
) : Serializable
