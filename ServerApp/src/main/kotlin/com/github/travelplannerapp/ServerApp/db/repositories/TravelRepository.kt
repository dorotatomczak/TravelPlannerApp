package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import org.springframework.stereotype.Component

@Component
class TravelRepository : ITravelRepository {
    companion object {
        const val tableName = "travel"
        const val columnId = "id"
        const val columnName = "name"
        private const val insertStatement = "INSERT INTO $tableName (id, name) VALUES (?, ?)"
        private const val selectStatement = "SELECT * FROM $tableName "
        private const val deleteStatement = "DELETE FROM $tableName "
        private const val updateStatement = "UPDATE $tableName SET name=? WHERE id=?"
    }

    override fun getAllTravelsByUserId(id: Int): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT $tableName.$columnId, $tableName.$columnName " +
                                "FROM $tableName INNER JOIN  ${UserTravelRepository.tableName} " +
                                "on $tableName.$columnId =  ${UserTravelRepository.tableName}.${UserTravelRepository.columnTravelId} " +
                                "where  ${UserTravelRepository.tableName}.${UserTravelRepository.columnUserId} = ?"
                )
        statement.setInt(1, id)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun getAllTravelsByUserEmail(email: String): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection.conn
                .prepareStatement(
                        "SELECT $tableName.$columnId, $tableName.$columnName " +
                                "FROM $tableName INNER JOIN ${UserTravelRepository.tableName} " +
                                "ON $tableName.$columnId = ${UserTravelRepository.tableName}.${UserTravelRepository.columnTravelId} " +
                                "INNER JOIN ${UserRepository.tableName} " +
                                "ON ${UserTravelRepository.tableName}.${UserTravelRepository.columnUserId} = ${UserRepository.tableName}.${UserTravelRepository.columnId} " +
                                "WHERE ${UserRepository.tableName}.email = ?"
                )
        statement.setString(1, email)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun get(id: Int): Travel? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        val result = statement.executeQuery()
        if (result.next()) {
            return Travel(result)
        }
        return null
    }

    override fun getAll(): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun add(obj: Travel): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setString(2, obj.name)
        return statement.executeUpdate() > 0
    }

    override fun delete(id: Int): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        return statement.executeUpdate() > 0
    }

    override fun deleteAll(): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement)
        return statement.executeUpdate() > 0
    }

    override fun update(obj: Travel): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.name)
        statement.setInt(2, obj.id!!)
        return statement.executeUpdate() > 0
    }

    override fun getNextId(): Int {
        val statement = DbConnection.conn
                .prepareStatement(
                        "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id;"
                )
        val result = statement.executeQuery()
        result.next()
        return result.getInt("new_id")
    }
}