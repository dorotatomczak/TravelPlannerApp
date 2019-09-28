package com.github.travelplannerapp.communication.model

import java.io.Serializable

class Place(
        var title: String,
        var position: Array<Double>,
        var vicinity: String,
        var category: ObjectCategory,
        var categoryTitle: String = "",
        var href: String = "",
        var resultType: String = "",
        var averageRating: String = "",
        var distance: Double = 0.0,
        val openingHours: OpeningHours? = null
) : Serializable

class ObjectCategory(
        var id: String = "",
        var title: String = "",
        var categoryIcon: Int = 0,
        var href: String = "",
        var type: String = "",
        var system: String = ""
) : Serializable

class OpeningHours(var text: String) : Serializable