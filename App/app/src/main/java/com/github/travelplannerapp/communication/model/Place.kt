package com.github.travelplannerapp.communication.model

import java.io.Serializable

data class Place (
    var title: String,
    var position: Array<Double>,
    var location: String,
    var category: PlaceCategory
) : Serializable
