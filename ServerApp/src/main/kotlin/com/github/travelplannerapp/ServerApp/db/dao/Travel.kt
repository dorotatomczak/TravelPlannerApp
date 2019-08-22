package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class Travel(var id: Int,
             var name: String) {
    constructor(result: ResultSet) :
            this(
                    result.getInt("id"),
                    result.getString("name")
            )
}