package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.UserPlace
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class UserPlaceRepository : Repository<UserPlace>(), IUserPlaceRepository {

    companion object {
        const val tableName = "app_user_place"
        const val columnId = "id"
        const val columnUserId = "app_user_id"
        const val columnPlaceId = "place_id"
        const val columnRating = "rating"
    }

    override val selectStatement = "SELECT * FROM $tableName "
    override val insertStatement = "INSERT INTO $tableName ($columnId, $columnUserId, " +
            "$columnPlaceId, $columnRating) VALUES (?, ?, ?, ?) "
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName SET $columnUserId=?, $columnPlaceId=?, " +
            "$columnRating=?  WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun T(result: ResultSet): UserPlace? {
        return UserPlace(result)
    }

    override fun prepareInsertStatement(obj: UserPlace): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setInt(2, obj.userId!!)
        statement.setInt(3, obj.placeId!!)
        statement.setInt(4, obj.rating!!)
        return statement
    }

    override fun prepareUpdateStatement(obj: UserPlace): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setInt(1, obj.userId!!)
        statement.setInt(2, obj.placeId!!)
        statement.setInt(3, obj.rating!!)
        statement.setInt(4, obj.id!!)
        return statement
    }

    override fun getUserPlaceByUserIdAndPlaceId(userId: Int, placeId: Int): UserPlace? {

        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT * FROM $tableName " +
                                "WHERE $columnUserId=? AND $columnPlaceId=?"
                )
        statement.setInt(1, userId)
        statement.setInt(2, placeId)

        val result = statement.executeQuery()
        if (result.next()) {
            return UserPlace(result)
        }
        return null
    }
}
