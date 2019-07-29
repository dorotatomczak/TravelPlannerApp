package com.github.travelplannerapp.ServerApp.db

import java.sql.Connection
import java.sql.DriverManager

object DbConnection {

    val conn: Connection = DriverManager
            .getConnection("jdbc:postgresql://localhost/travelplanner?user=postgres&password=password")
}