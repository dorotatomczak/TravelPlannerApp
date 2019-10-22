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
        const val columnHref = "href"
        const val columnTitle = "title"
        const val columnVicinity = "vicinity"
        const val columnCategory = "category"
        const val columnAverageRating = "average_rating"
        const val columnRatesCount = "rates_count"
    }

    override val selectStatement = "SELECT * FROM $tableName "
    override val insertStatement =
        "INSERT INTO $tableName ($columnId, $columnHereId, $columnHref, $columnTitle, $columnVicinity, $columnCategory, $columnAverageRating, $columnRatesCount)" +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement =
        "UPDATE $tableName SET $columnHereId=?, $columnHref=?, $columnTitle=?, $columnVicinity=?, $columnCategory=?, $columnAverageRating=?, $columnRatesCount=? WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun getPlaceByHereId(hereId: String): PlaceDao? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnHereId=?")
        statement.setString(1, hereId)
        val result = statement.executeQuery()
        return if (result.next()) PlaceDao(result)
        else null
    }

    override fun T(result: ResultSet): PlaceDao? {
        return PlaceDao(result)
    }

    override fun prepareInsertStatement(obj: PlaceDao): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setString(2, obj.hereId)
        statement.setString(3, obj.href)
        statement.setString(4, obj.title)
        statement.setString(5, obj.vicinity)
        statement.setInt(6, obj.category!!)
        statement.setDouble(7, obj.averageRating!!)
        statement.setInt(8, obj.ratesCount!!)
        return statement
    }

    override fun prepareUpdateStatement(obj: PlaceDao): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.hereId)
        statement.setString(2, obj.href)
        statement.setString(3, obj.title)
        statement.setString(4, obj.vicinity)
        statement.setInt(5, obj.category!!)
        statement.setDouble(6, obj.averageRating!!)
        statement.setInt(7, obj.ratesCount!!)
        // id at the end
        statement.setInt(8, obj.id!!)
        return statement
    }
}
