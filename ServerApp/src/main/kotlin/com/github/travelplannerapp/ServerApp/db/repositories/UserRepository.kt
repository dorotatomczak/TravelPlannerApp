package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.Timestamp

@Component
class UserRepository : IUserRepository {

    override fun getUserByEmail(email: String): User? {
        val statement = DbConnection
                .conn
                .prepareStatement("SELECT * FROM app_user WHERE email=?")
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
                .prepareStatement("SELECT * FROM app_user WHERE id=?")
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
                .prepareStatement("SELECT * FROM app_user")
        val result = statement.executeQuery()
        while (result.next()) {
            users.add(User(result))
        }
        return users
    }

    override fun add(obj: User) {
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "INSERT INTO app_user (name,email,password,authToken,expirationDate) " +
                                "VALUES (?,?,?,?,?)"
                )
        statement.setString(1, obj.email)
        statement.setString(2, obj.password)
        statement.setString(3, obj.authToken)
        statement.setTimestamp(4, obj.expirationDate)
        statement.executeUpdate()
    }

    override fun add(objs: MutableList<User>) {
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "INSERT INTO app_user (name,email,password,authToken,expirationDate) " +
                                "VALUES (?,?,?,?,?)"
                )
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

    override fun updateUserAuthByEmail(email: String, authToken: String, expirationDate: Timestamp) {
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "UPDATE app_user " +
                                "SET expirationdate = ?, " +
                                "authtoken = ? " +
                                "WHERE email = ? "
                )
        statement.setTimestamp(1, expirationDate)
        statement.setString(2, authToken)
        statement.setString(3, email)
        statement.executeUpdate()
    }

    override fun delete(id: Int) {
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "DELETE FROM app_user " +
                                "WHERE id=?"
                )
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    override fun deleteAll() {
        val statement = DbConnection
                .conn
                .prepareStatement("DELETE FROM app_user")
        statement.executeUpdate()
    }
}