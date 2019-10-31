package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class UserRepository : Repository<User>(), IUserRepository {
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

    override fun getAllFriendsByUserId(id: Int): MutableList<User> {
        val friends = mutableListOf<User>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT * FROM $tableName " +
                                "INNER JOIN ${UserFriendRepository.tableName} " +
                                "on $tableName.$columnId = ${UserFriendRepository.tableName}.${UserFriendRepository.columnFriendId} " +
                                "WHERE ${UserFriendRepository.tableName}.${UserFriendRepository.columnUserId}=?"
                )
        statement.setInt(1, id)
        val result = statement.executeQuery()
        while (result.next()) {
            friends.add(User(result))
        }
        return friends
    }

    override fun findMatchingEmails(query: String, userId: Int): MutableList<User> {
        val users = mutableListOf<User>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT * FROM $tableName " +
                                "WHERE $tableName.$columnEmail LIKE ? " +
                                "AND $tableName.$columnId !=?" +
                                "AND $tableName.$columnId NOT IN " +
                                "(SELECT ${UserFriendRepository.tableName}.${UserFriendRepository.columnFriendId} " +
                                "FROM ${UserFriendRepository.tableName} " +
                                "WHERE ${UserFriendRepository.tableName}.${UserFriendRepository.columnUserId} =?); "
                )
        statement.setString(1, "%$query%")
        statement.setInt(2, userId)
        statement.setInt(3, userId)
        val result = statement.executeQuery()
        while (result.next()) {
            users.add(User(result))
        }
        return users
    }

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
}


