package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.datamodels.UserInfo
import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class UserRepository : Repository<User>(), IUserRepository {
    override fun findEmails(searchString: String): MutableList<UserInfo> {
        val usersEmails = mutableListOf<UserInfo>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT * FROM $tableName " +
                                "WHERE $tableName.$columnEmail LIKE ?"
                )
        statement.setString(1, "%$searchString%")
        val result = statement.executeQuery()
        var u: User
        while (result.next()) {
            u = User(result)

            usersEmails.add(UserInfo(u.id.toString().toInt(), u.email.toString()))
        }

        return usersEmails

    }


    companion object {
        const val tableName = "app_user"
        const val columnId = "id"
        const val columnEmail = "email"
        const val columnPassword = "password"
    }

    override val selectStatement = "SELECT * FROM $tableName "
    override val insertStatement = "INSERT INTO $tableName " +
            "($columnId,$columnEmail,$columnPassword) " +
            "VALUES (?,?,?) "
    override val deleteStatement = "DELETE FROM $tableName "
    override val updateStatement = "UPDATE $tableName " +
            "SET $columnEmail=?, $columnPassword=? " +
            "WHERE $columnId=? "
    override val nextIdStatement = "SELECT nextval(pg_get_serial_sequence('$tableName', '$columnId')) AS new_id"


    override fun T(result: ResultSet): User? {
        return User(result)
    }

    override fun prepareInsertStatement(obj: User): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id!!)
        statement.setString(2, obj.email)
        statement.setString(3, obj.password)
        return statement
    }

    override fun prepareUpdateStatement(obj: User): PreparedStatement {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.email)
        statement.setString(2, obj.password)
                statement.setInt(3, obj.id!!)
        return statement
    }

        statement.setString(3, obj.token)
        statement.setTimestamp(4, obj.expirationDate)
        statement.setInt(5, obj.id!!)
        return statement
    }

    override fun getUserByEmail(email: String): User? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnEmail=?")
        statement.setString(1, email)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return User(result)
        }
        return null
    }

    override fun getAllFriendsByUserId(id: Int): MutableList<UserInfo> {
        val friends = mutableListOf<UserInfo>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT * FROM $tableName "+
                                "INNER JOIN ${UserFriendRepository.tableName} "+
                                "on $tableName.$columnId = ${UserFriendRepository.tableName}.${UserFriendRepository.columnFriendId}"+
                                " WHERE ${UserFriendRepository.tableName}.${UserFriendRepository.columnUserId}=?"
                )
        statement.setInt(1, id)
        val result = statement.executeQuery()
        while (result.next()) {
            var friend=User(result)
            friends.add(UserInfo(friend.id!!,friend.email!!))
        }
        return friends
    }

}


