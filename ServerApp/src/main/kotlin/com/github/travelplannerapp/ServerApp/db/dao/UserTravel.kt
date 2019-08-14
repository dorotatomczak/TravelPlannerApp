package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class UserTravel (
        val userId: Int,
        val travelId: Int,
        val id: Int = -1
){
    constructor(result: ResultSet) :
            this(
                    result.getInt(2),
                    result.getInt(3),
                    result.getInt(1)
            )
}