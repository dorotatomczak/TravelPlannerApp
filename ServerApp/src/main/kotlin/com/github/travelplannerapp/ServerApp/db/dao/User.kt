package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class User(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var email: String? by defaultMap
    var password: String? by defaultMap

    constructor(id: Int?,
                email: String?,
                password: String?) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "email" to email,
                            "password" to password)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "email" to result.getString("email"),
                            "password" to result.getString("password")
                    )
            )
}
