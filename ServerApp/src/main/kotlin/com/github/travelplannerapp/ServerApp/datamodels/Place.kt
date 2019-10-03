package com.github.travelplannerapp.ServerApp.datamodels

import java.io.Serializable

// place has only some fields of original response, it can be more if needed
class Place(
        val id: String,
        var title: String,
        var vicinity: String,
        val position: Array<Double>,
        val categoryNumber: Int,
        val category: ObjectCategory,
        val href: String,
        val resultType: String = "",
        val averageRating: String = "",
        val distance: Double = 0.0,
        val openingHours: OpeningHours? = null
)

// some variables might be unused and meant to be deleted, for now I got all of them
class ObjectCategory(
        val id: String = "",
        val title: String = "",
        val href: String = "",
        val type: String = "",
        val system: String = ""
)

class OpeningHours(var text: String)
