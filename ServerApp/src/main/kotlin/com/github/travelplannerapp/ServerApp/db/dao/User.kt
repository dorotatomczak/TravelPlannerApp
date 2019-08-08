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
                result.getString(2),
                result.getString(3),
                result.getString(4),
                result.getTimestamp(5),
                result.getInt(1)
            )
}