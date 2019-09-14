package com.github.travelplannerapp.communication.model

import java.io.Serializable
import java.util.*

data class Plan (
        var locale: Locale,
        var fromDateTime: Calendar,
        var toDateTime: Calendar,
        var place: Place
) : Serializable
