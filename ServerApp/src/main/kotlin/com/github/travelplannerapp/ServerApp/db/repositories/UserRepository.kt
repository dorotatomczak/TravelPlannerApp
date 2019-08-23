package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserRepository : IUserRepository {
    companion object {
        private const val insertStatement = "INSERT INTO app_user (id, email,password,authToken,expirationDate) " +
                "VALUES (?,?,?,?,?) "
        private const val selectStatement = "SELECT * FROM app_user "
        private const val deleteStatement = "DELETE FROM app_user "
        private const val updateStatement = "UPDATE app_user "
    }

    override fun getUserByEmail(email: String): User? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE email=?")
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
                .prepareStatement(selectStatement + "WHERE id=?")
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
        statement.setString(4, obj.authToken)
        statement.setTimestamp(5, obj.expirationDate)
        return statement.executeUpdate() > 0
    }

    //TODO [Magda] ovveride
    fun update(obj: User): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(updateStatement + "SET email = ?, " +
                        "password = ?, " +
                        "authtoken = ?, " +
                        "expirationdate = ? " +
                        " WHERE id = ?")
        statement.setString(1, obj.email)
        statement.setString(2, obj.password)
        statement.setString(3, obj.authToken)
        statement.setTimestamp(4, obj.expirationDate)
        statement.setInt(5,obj.id!!)
        return statement.executeUpdate() > 0
    }

    override fun delete(id: Int): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement + "WHERE id=?")
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
                        "SELECT nextval(pg_get_serial_sequence('app_user', 'id')) AS new_id;"
                )
        val result = statement.executeQuery()
        result.next()
        return result.getInt("new_id")
    }
}