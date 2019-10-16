package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class PlaceDao(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var hereId: String? by defaultMap
    var href: String? by defaultMap
    var title: String? by defaultMap
    var vicinity: String? by defaultMap
    var category: Int? by defaultMap
    var rating: Double? by defaultMap
    var rateCount: Int? by defaultMap

    constructor(
            id: Int?,
            hereId: String?,
            href: String?,
            title: String?,
            vicinity: String?,
            category: Int?,
            rating: Double?,
            rateCount: Int?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "hereId" to hereId,
                            "href" to href,
                            "title" to title,
                            "vicinity" to vicinity,
                            "category" to category,
                            "rating" to rating,
                            "rateCount" to rateCount)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "hereId" to result.getString("here_id"),
                            "href" to result.getString("href"),
                            "title" to result.getString("title"),
                            "vicinity" to  result.getString("vicinity"),
                            "category" to  result.getInt("category"),
                            "rating" to  result.getDouble("rating"),
                            "rateCount" to  result.getInt("rateCount")
                    )
            )
}
