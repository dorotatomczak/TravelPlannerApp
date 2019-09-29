package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class PlaceDao(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var hereId: String? by defaultMap
    var title: String? by defaultMap
    var location: String? by defaultMap
    var category: String? by defaultMap

    constructor(
            id: Int?,
            hereId: String?,
            title: String?,
            location: String?,
            category: String?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "hereId" to hereId,
                            "title" to title,
                            "location" to location,
                            "category" to category)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "hereId" to result.getString("here_id"),
                            "title" to result.getString("title"),
                            "location" to  result.getString("location"),
                            "category" to  result.getString("category")
                    )
            )
}
