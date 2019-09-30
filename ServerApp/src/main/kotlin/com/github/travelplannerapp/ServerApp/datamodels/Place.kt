package com.github.travelplannerapp.ServerApp.datamodels

import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import java.io.Serializable

// place has only some fields of original response, it can be more if needed
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
): Serializable {

    //TEMPORARY
    fun printPlace() {
        println("Place: ")
        println(id)
        println(title)
        println(vicinity)
        println(position)
        println("Cat id: " + category.id)
        println("Cat: " + category.title)
        println(href)
        println(resultType)
        println(averageRating)
        println(distance.toString())
    }
}

// some variables might be unused and meant to be deleted, for now I got all of them
class ObjectCategory(
        var id: String = "",
        var title: String = "",
        var href: String = "",
        var type: String = "",
        var system: String = ""
): Serializable
