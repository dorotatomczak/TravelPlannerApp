package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class UserPlace(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var userId: Int? by defaultMap
    var placeId: Int? by defaultMap
    var rating: Int? by defaultMap

    constructor(id: Int?, userId: Int?, placeId: Int?, rating: Int?) :
            this(
                    mutableMapOf<String, Any?>(
                            "id" to id,
                            "userId" to userId,
                            "placeId" to placeId,
                            "rating" to rating)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf<String, Any?>(
                            "id" to result.getInt("id"),
                            "userId" to result.getInt("app_user_id"),
                            "placeId" to result.getInt("place_id"),
                            "rating" to result.getInt("rating")
                    )
            )
}
