package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class UserTravel (
        val userId: Int,
        val travelId: Int,
        val id: Int = -1
){
    constructor(result: ResultSet) :
            this(
                    result.getInt("app_user_id"),
                    result.getInt("travel_id"),
                    result.getInt("id")
            )
}