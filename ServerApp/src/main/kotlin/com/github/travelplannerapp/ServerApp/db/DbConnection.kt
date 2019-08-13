package com.github.travelplannerapp.ServerApp.db

import java.sql.Connection
import java.sql.DriverManager

object DbConnection {

    val conn: Connection = DriverManager
            .getConnection("jdbc:postgresql://balarama.db.elephantsql.com:5432/tpcqgcqq?user=tpcqgcqq&password=q1aVOwAJo98H9NcLE4zLIXarlPaIi46e")
}