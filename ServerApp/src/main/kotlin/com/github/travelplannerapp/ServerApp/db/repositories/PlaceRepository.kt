package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.PlaceDao
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class PlaceRepository : Repository<PlaceDao>(), IPlaceRepository {
    companion object {
        const val tableName = "place"
        const val columnId = "id"
        const val columnHereId = "here_id"
        const val columnTitle = "title"
        const val columnLocation = "location"
        const val columnCategory = "category"
    }

    override val selectStatement = "SELECT * FROM $tableName "
    override val insertStatement = "INSERT INTO $tableName ($columnId, $columnHereId, $columnTitle, $columnLocation, $columnCategory)" +
            " VALUES (?, ?, ?, ?, ?)"
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName SET $columnHereId, $columnTitle=?, $columnLocation=?, $columnCategory=? WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun T(result: ResultSet): PlaceDao? {
        return PlaceDao(result)
    }

    override fun prepareInsertStatement(obj: PlaceDao): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setString(2, obj.hereId)
        statement.setString(3, obj.title)
        statement.setString(4, obj.location)
        statement.setString(5, obj.category)
        return statement
    }

    override fun prepareUpdateStatement(obj: PlaceDao): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.hereId)
        statement.setString(2, obj.title)
        statement.setString(3, obj.location)
        statement.setString(4, obj.category)
        statement.setInt(5, obj.id!!)
        return statement
    }
}
