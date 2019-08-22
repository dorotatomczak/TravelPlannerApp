package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet
import java.sql.Timestamp


class User(
        var id: Int,
        var email: String,
        var password: String,
        var authToken: String? = null,
        var expirationDate: Timestamp? = null
        ) {
    constructor(result: ResultSet) :
            this(
                    result.getInt("id"),
                    result.getString("email"),
                    result.getString("password"),
                    result.getString("authtoken"),
                    result.getTimestamp("expirationdate")
            )
}