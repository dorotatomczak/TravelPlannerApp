package com.github.travelplannerapp.ServerApp.db

import org.postgresql.ds.PGPoolingDataSource
import java.sql.Connection

object DbConnection {

    val dataSource = PGPoolingDataSource()
    var conn: Connection

    init {
        dataSource.serverName = "balarama.db.elephantsql.com"
        dataSource.portNumber = 5432
        dataSource.databaseName = "tpcqgcqq"
        dataSource.user = "tpcqgcqq"
        dataSource.password = "q1aVOwAJo98H9NcLE4zLIXarlPaIi46e"
        dataSource.maxConnections = 10

        conn = dataSource.connection
    }
}