package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class PlaceDao(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var hereId: String? by defaultMap
    var href: String? by defaultMap
    var title: String? by defaultMap
    var vicinity: String? by defaultMap
    var category: String? by defaultMap

    constructor(
            id: Int?,
            hereId: String?,
            href: String?,
            title: String?,
            vicinity: String?,
            category: String?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "hereId" to hereId,
                            "href" to href,
                            "title" to title,
                            "vicinity" to vicinity,
                            "category" to category)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "hereId" to result.getString("here_id"),
                            "href" to result.getString("href"),
                            "title" to result.getString("title"),
                            "vicinity" to  result.getString("vicinity"),
                            "category" to  result.getString("category")
                    )
            )
}
