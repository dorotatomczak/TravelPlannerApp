package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class Travel(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var name: String? by defaultMap

    constructor(id: Int?, name: String?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "name" to name)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "name" to result.getString("name")
                    )
            )
}