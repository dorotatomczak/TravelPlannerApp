package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet
import java.sql.Timestamp


class User(
    var id: Int = -1,
    var email: String,
    var password: String,
    var authToken: String?,
    val expirationDate: Timestamp?
) {
    constructor(result: ResultSet) :
            this(
                result.getInt(1),
                result.getString(2),
                result.getString(3),
                result.getString(4),
                result.getTimestamp(5)
            )
}