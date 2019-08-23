package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserTravelRepository : IUserTravelRepository {
    companion object {
        const val tableName = "app_user_travel"
        const val columnId = "id"
        const val columnUserId = "app_user_id"
        const val columnTravelId = "travel_id"
        private const val insertStatement = "INSERT INTO $tableName ($columnId, $columnUserId, $columnTravelId) " +
                "VALUES (?, ?, ?) "
        private const val selectStatement = "SELECT * FROM $tableName "
        private const val deleteStatement = "DELETE FROM $tableName "
        private const val updateStatement = "UPDATE $tableName SET $columnUserId=?, $columnTravelId=?  WHERE $columnId=?"
    }

    override fun get(id: Int): UserTravel? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return UserTravel(result)
        }
        return null
    }

    override fun getAll(): MutableList<UserTravel> {
        var userTravels = mutableListOf<UserTravel>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            userTravels.add(UserTravel(result))
        }
        return userTravels
    }

    override fun add(obj: UserTravel): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setInt(2, obj.userId!!)
        statement.setInt(3, obj.travelId!!)
        return statement.executeUpdate() > 0
    }

    override fun update(obj: UserTravel): Boolean{
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setInt(1, obj.userId!!)
        statement.setInt(2, obj.travelId!!)
        statement.setInt(3, obj.id!!)
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