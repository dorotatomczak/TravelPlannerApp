package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.UserFriend
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class UserFriendRepository : Repository<UserFriend>(), IUserFriendRepository {

    companion object {
        const val tableName = "app_user_friend"
        const val columnId = "id"
        const val columnUserId = "app_user_id"
        const val columnFriendId = "friend_user_id"
    }

    override val selectStatement = "SELECT * FROM $tableName "
    override val insertStatement = "INSERT INTO $tableName ($columnId, $columnUserId, $columnFriendId) " +
            "VALUES (?, ?, ?) "
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName SET $columnUserId=?, $columnFriendId=?  WHERE $columnId=?"
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"

    override fun ifUserFriendBindingExist(userId: Int, friendId: Int): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement("SELECT $columnId FROM $tableName WHERE $columnUserId=? AND $columnFriendId=?")
        statement.setInt(1, userId)
        statement.setInt(2, friendId)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return true
        }
        return false;
    }

    override fun deleteUserFriendBinding(userId: Int, friendId: Int): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement + "WHERE $columnUserId=? AND $columnFriendId=?")
        statement.setInt(1, userId)
        statement.setInt(2, friendId)
        return statement.executeUpdate() > 0
    }

    override fun T(result: ResultSet): UserFriend? {
        return UserFriend(result)
    }

    override fun prepareInsertStatement(obj: UserFriend): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setInt(2, obj.userId!!)
        statement.setInt(3, obj.friendId!!)
        return statement
    }

    override fun prepareUpdateStatement(obj: UserFriend): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setInt(1, obj.userId!!)
        statement.setInt(2, obj.friendId!!)
        statement.setInt(3, obj.id!!)
        return statement
    }
}
