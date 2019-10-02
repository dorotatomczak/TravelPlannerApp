package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class UserTravel(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var userId: Int? by defaultMap
    var travelId: Int? by defaultMap

    constructor(id: Int?, userId: Int?, travelId: Int?) :
            this(
                    mutableMapOf<String, Any?>(
                            "id" to id,
                            "userId" to userId,
                            "travelId" to travelId)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf<String, Any?>(
                            "id" to result.getInt("id"),
                            "userId" to result.getInt("app_user_id"),
                            "travelId" to result.getInt("travel_id")
                    )
            )
}