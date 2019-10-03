package com.github.travelplannerapp.ServerApp.datamodels

import java.io.Serializable

// place has only some fields of original response, it can be more if needed
class Place(
        var id: String,
        var title: String,
        var vicinity: String,
        var position: Array<Double>,
        var categoryNumber: Int,
        var category: ObjectCategory,
        var href: String = "",
        var resultType: String = "",
        var averageRating: String = "",
        var distance: Double = 0.0,
        var openingHours: OpeningHours? = null
) : Serializable

// some variables might be unused and meant to be deleted, for now I got all of them
class ObjectCategory(
        var id: String = "",
        var title: String = "",
        var href: String = "",
        var type: String = "",
        var system: String = ""
) : Serializable

class OpeningHours(var text: String)
