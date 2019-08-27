package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class Scan(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var userId: Int? by defaultMap
    var travelId: Int? by defaultMap
    var name: String? by defaultMap

    constructor(id: Int?,
                userId: Int?,
                travelId: Int?,
                name: String?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "userId" to userId,
                            "travelId" to travelId,
                            "name" to name)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "userId" to result.getInt("user_Id"),
                            "travelId" to result.getInt("travel_Id"),
                            "name" to result.getString("name")
                    )
            )
}