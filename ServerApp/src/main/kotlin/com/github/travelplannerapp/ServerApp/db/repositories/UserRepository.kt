package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserRepository : IUserRepository {

    override fun getUserByEmail(email: String): User? {
        val statement = DbConnection
                        .conn
                        .prepareStatement("SELECT * FROM app_user WHERE email=?")
        statement.setString(1, email)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return User(result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4))
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
            return User(result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4))
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
            var user = User(result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4))
            users.add(user)
        }
        return users
    }

    override fun add(obj: User) {
        val statement = DbConnection
                        .conn
                        .prepareStatement("INSERT INTO app_user (name,email,password) " +
                                "VALUES (?,?,?)")
        statement.setString(1, obj.name)
        statement.setString(2, obj.email)
        statement.setString(3, obj.password)
        statement.executeUpdate()
    }

    override fun add(objs: MutableList<User>) {
        val statement = DbConnection
                        .conn
                        .prepareStatement("INSERT INTO app_user (name,email,password) " +
                                "VALUES (?,?,?)")
        objs.iterator().forEach { obj ->
            run {
                statement.setString(1, obj.name)
                statement.setString(2, obj.email)
                statement.setString(3, obj.password)
                statement.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        val statement = DbConnection
                        .conn
                        .prepareStatement("DELETE FROM app_user " +
                                "WHERE id=?")
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