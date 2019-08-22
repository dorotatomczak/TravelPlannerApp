package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class UserTravel(
        val id: Int,
        val userId: Int,
        val travelId: Int) {
    constructor(result: ResultSet) :
            this(
                    result.getInt("id"),
                    result.getInt("app_user_id"),
                    result.getInt("travel_id")
            )
}