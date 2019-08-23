package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class UserTravelRepository : Repository<UserTravel>(), IUserTravelRepository {
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

    fun getAll(): MutableList<UserTravel> {
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

    override fun T(result: ResultSet): UserTravel? {
        return UserTravel(result)
    }

    override fun prepareSelectByIdStatement(id: Int): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        return statement
    }

    override fun prepareInsertStatement(obj: UserTravel): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setInt(2, obj.userId!!)
        statement.setInt(3, obj.travelId!!)
        return statement
    }

    override fun prepareUpdateStatement(obj: UserTravel): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setInt(1, obj.userId!!)
        statement.setInt(2, obj.travelId!!)
        statement.setInt(3, obj.id!!)
        return statement
    }

    override fun prepareDeleteByIdStatement(id: Int): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        return statement
    }

    override fun prepareDeleteAllStatement(): PreparedStatement {
        return DbConnection
                .conn
                .prepareStatement(deleteStatement)
    }

    override fun prepareNextIdStatement(): PreparedStatement {
        return DbConnection.conn
                .prepareStatement(
                        "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id;"
                )
    }
}