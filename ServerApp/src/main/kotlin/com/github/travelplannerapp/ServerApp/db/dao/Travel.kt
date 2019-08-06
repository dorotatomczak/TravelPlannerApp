package com.github.travelplannerapp.ServerApp.db.dao

import java.sql.ResultSet

class Travel (var id: Int = -1,
              var name: String) {
    constructor(result: ResultSet) :
            this(
                result.getInt(1),
                result.getString(2)
            )
}