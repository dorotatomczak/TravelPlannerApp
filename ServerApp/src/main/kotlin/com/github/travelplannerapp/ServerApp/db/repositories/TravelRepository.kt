package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class TravelRepository : Repository<Travel>(), ITravelRepository {
    companion object {
        const val tableName = "travel"
        const val columnId = "id"
        const val columnName = "name"
    }

    override val selectStatement = "SELECT * FROM $tableName "
    override val insertStatement = "INSERT INTO $tableName ($columnId, $columnName) VALUES (?, ?)"
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName SET $columnName=? WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun getAllTravelsByUserId(id: Int): MutableList<Travel> {
        val travels = mutableListOf<Travel>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT $tableName.$columnId, $tableName.$columnName " +
                                "FROM $tableName INNER JOIN ${UserTravelRepository.tableName} " +
                                "on $tableName.$columnId = ${UserTravelRepository.tableName}.${UserTravelRepository.columnTravelId} " +
                                "where ${UserTravelRepository.tableName}.${UserTravelRepository.columnUserId} = ?"
                )
        statement.setInt(1, id)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun getAllTravelsByUserEmail(email: String): MutableList<Travel> {
        val travels = mutableListOf<Travel>()
        val statement = DbConnection.conn
                .prepareStatement(
                        "SELECT $tableName.$columnId, $tableName.$columnName " +
                                "FROM $tableName INNER JOIN ${UserTravelRepository.tableName} " +
                                "ON $tableName.$columnId = ${UserTravelRepository.tableName}.${UserTravelRepository.columnTravelId} " +
                                "INNER JOIN ${UserRepository.tableName} " +
                                "ON ${UserTravelRepository.tableName}.${UserTravelRepository.columnUserId} = ${UserRepository.tableName}.${UserTravelRepository.columnId} " +
                                "WHERE ${UserRepository.tableName}.${UserRepository.columnEmail} = ?"
                )
        statement.setString(1, email)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun T(result: ResultSet): Travel? {
        return Travel(result)
    }

    override fun prepareInsertStatement(obj: Travel): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setString(2, obj.name)
        return statement
    }

    override fun prepareUpdateStatement(obj: Travel): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.name)
        statement.setInt(2, obj.id!!)
        return statement
    }
}
