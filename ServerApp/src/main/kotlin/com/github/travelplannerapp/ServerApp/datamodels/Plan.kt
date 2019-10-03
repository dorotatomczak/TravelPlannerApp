package com.github.travelplannerapp.ServerApp.datamodels

import java.io.Serializable

data class Plan(
        var id: Int,
        var locale: String, // time format that allows time to be correct in every time zone
        var fromDateTimeMs: Long,
        var toDateTimeMs: Long,
        var placeId: Int,
        var place: Place
) : Serializable
