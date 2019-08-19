package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet
import java.sql.Timestamp


class User(
    var email: String,
    var password: String,
    var authToken: String? = null,
    val expirationDate: Timestamp? = null,
    var id: Int = -1
) {
    constructor(result: ResultSet) :
            this(
                result.getString("email"),
                result.getString("password"),
                result.getString("authtoken"),
                result.getTimestamp("expirationdate"),
                result.getInt("id")
            )
}