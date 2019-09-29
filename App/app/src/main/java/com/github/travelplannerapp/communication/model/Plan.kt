package com.github.travelplannerapp.communication.model

import java.io.Serializable
import java.util.*

data class Plan (
        val id: Int,
        var locale: Locale, // time format that allows time to be correct in every time zone
        var fromDateTime: Calendar,
        var toDateTime: Calendar,
        var place: Place
) : Serializable
