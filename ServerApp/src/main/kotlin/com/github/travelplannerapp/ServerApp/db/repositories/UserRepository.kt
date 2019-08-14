package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.Timestamp

@Component
class UserRepository : IUserRepository {
    companion object {
        const val insertStatement = "INSERT INTO app_user (email,password,authToken,expirationDate) " +
                "VALUES (?,?,?,?) "
        const val selectStatement = "SELECT * FROM app_user "
        const val deleteStatement = "DELETE FROM app_user "
        const val updateStatement = "UPDATE app_user "
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
        statement.setString(1, obj.email)
        statement.setString(2, obj.password)
        statement.setString(3, obj.authToken)
        statement.setTimestamp(4, obj.expirationDate)
        return statement.executeUpdate() > 0
    }

    override fun add(objs: MutableList<User>) {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        objs.iterator().forEach { obj ->
            run {
                statement.setString(1, obj.email)
                statement.setString(2, obj.password)
                statement.setString(3, obj.authToken)
                statement.setTimestamp(4, obj.expirationDate)
                statement.executeUpdate()
            }
        }
    }

    override fun updateUserAuthByEmail(email: String, authToken: String, expirationDate: Timestamp): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(
                        updateStatement +
                                "SET expirationdate = ?, " +
                                "authtoken = ? " +
                                "WHERE email = ? "
                )
        statement.setTimestamp(1, expirationDate)
        statement.setString(2, authToken)
        statement.setString(3, email)
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
}