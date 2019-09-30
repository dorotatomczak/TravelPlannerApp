package com.github.travelplannerapp.communication.model

import java.io.Serializable

class Place(
        var id: String,
        var title: String,
        var vicinity: String,
        var position: Array<Double>,
        var category: ObjectCategory,
        var href: String = "",
        var categoryTitle: String = "",
        var resultType: String = "",
        var averageRating: String = "",
        var distance: Double = 0.0
): Serializable

class ObjectCategory(
        var id: String = "",
        var title: String = "",
        var href: String = "",
        var type: String = "",
        var system: String = ""
): Serializable
