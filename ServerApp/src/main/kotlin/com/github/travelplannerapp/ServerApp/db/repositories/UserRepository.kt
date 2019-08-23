package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserRepository : IUserRepository {
    companion object {
        const val tableName = "app_user"
        const val columnId = "id"
        const val columnEmail = "email"
        const val columnPassword = "password"
        const val columnAuthToken = "authtoken"
        const val columnExpirationDate = "expirationdate"
        private const val insertStatement = "INSERT INTO $tableName " +
                "($columnId,$columnEmail,$columnPassword,$columnAuthToken,$columnExpirationDate) " +
                "VALUES (?,?,?,?,?) "
        private const val selectStatement = "SELECT * FROM $tableName "
        private const val deleteStatement = "DELETE FROM $tableName "
        private const val updateStatement = "UPDATE $tableName " +
                "SET $columnEmail=?, $columnPassword=?, $columnAuthToken=?, $columnExpirationDate=?" +
                " WHERE $columnId=?"
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

    override fun get(id: Int): User? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE $columnId=?")
        statement.setInt(1, id)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return User(result)
        }
        return null
    }

    override fun getAll(): MutableList<User> {
        var users = mutableListOf<User>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            users.add(User(result))
        }
        return users
    }

    override fun add(obj: User): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1,obj.id!!)
        statement.setString(2, obj.email)
        statement.setString(3, obj.password)
        statement.setString(4, obj.token)
        statement.setTimestamp(5, obj.expirationDate)
        return statement.executeUpdate() > 0
    }

    override fun update(obj: User): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement)
        statement.setString(1, obj.email)
        statement.setString(2, obj.password)
        statement.setString(3, obj.token)
        statement.setTimestamp(4, obj.expirationDate)
        statement.setInt(5,obj.id!!)
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