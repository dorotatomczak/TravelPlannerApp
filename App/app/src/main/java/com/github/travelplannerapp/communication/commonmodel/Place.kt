package com.github.travelplannerapp.ServerApp.datamodels.commonmodel

import java.io.Serializable

class Place(
        var id: String = "",
        var title: String,
        var vicinity: String,
        var position: Array<Double>,
        var href: String,
        var categoryIcon: Int,
        var category: ObjectCategory = ObjectCategory(),
        var categoryTitle: String? = "",
        var resultType: String? = "",
        var averageRating: String? = "",
        var distance: Double? = 0.0,
        val openingHours: OpeningHours? = null
) : Serializable

class ObjectCategory(
        var id: String = "",
        var title: String = "",
        var href: String = "",
        var type: String = "",
        var system: String = ""
) : Serializable

class OpeningHours(var text: String = "") : Serializable
