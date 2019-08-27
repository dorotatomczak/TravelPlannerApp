package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class Scan(var id: Int,
           var userId: Int,
           var travelId: Int,
           var name: String) {

    constructor(result: ResultSet) :
            this(
                    result.getInt("id"),
                    result.getInt("user_id"),
                    result.getInt("travel_id"),
                    result.getString("name")
            )
}