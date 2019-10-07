package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Scan
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class ScanRepository : Repository<Scan>(), IScanRepository {
    companion object {
        const val tableName = "scan"
        const val columnId = "id"
        const val columnUserId = "app_user_id"
        const val columnTravelId = "travel_id"
        const val columnName = "name"
    }

    override val insertStatement = "INSERT INTO $tableName ($columnId, $columnUserId, $columnTravelId, $columnName) " +
            "VALUES (?, ?, ?, ?) "
    override val selectStatement = "SELECT * FROM $tableName "
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName SET $columnUserId=?, $columnTravelId=?, $columnName=? " +
            "WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun getAll(userId: Int, travelId: Int): MutableList<Scan> {
        val scans = mutableListOf<Scan>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT * FROM $tableName where  $tableName.$columnUserId = ? AND  $tableName.$columnTravelId = ?"
                )
        statement.setInt(1, userId)
        statement.setInt(2, travelId)
        val result = statement.executeQuery()
        while (result.next()) {
            scans.add(Scan(result))
        }
        return scans
    }

    override fun T(result: ResultSet): Scan? {
        return Scan(result)
    }

    override fun prepareInsertStatement(obj: Scan): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setInt(2, obj.userId!!)
        statement.setInt(3, obj.travelId!!)
        statement.setString(4, obj.name)
        return statement
    }

    override fun prepareUpdateStatement(obj: Scan): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setInt(1, obj.userId!!)
        statement.setInt(2, obj.travelId!!)
        statement.setString(3, obj.name)
        statement.setInt(4, obj.id!!)
        return statement
    }
}
