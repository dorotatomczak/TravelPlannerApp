package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet
import java.sql.Timestamp
class User(map: MutableMap<String, Any?>) {
    private val defaultMap = map.withDefault { null }

    var id: Int? by defaultMap
    var email: String? by defaultMap
    var password: String? by defaultMap
    var authToken: String? by defaultMap
    var expirationDate: Timestamp? by defaultMap

    constructor(id: Int?,
                email: String?,
                password: String?,
                authToken: String? = null,
                expirationDate: Timestamp? = null) :
            this(
                    mutableMapOf(
                            "id" to id,
                            "email" to email,
                            "password" to password,
                            "authToken" to authToken,
                            "expirationDate" to expirationDate)
            )

    constructor(result: ResultSet) :
            this(
                    mutableMapOf(
                            "id" to result.getInt("id"),
                            "email" to result.getString("email"),
                            "password" to result.getString("password"),
                            "authToken" to result.getString("authtoken"),
                            "expirationDate" to result.getTimestamp("expirationdate")
                    )
            )
}