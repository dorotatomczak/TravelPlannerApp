package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.User
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserRepository : IUserRepository {

    override fun get(id: Int): User? {
        val ps = DbConnection
                .conn
                .prepareStatement("SELECT * FROM app_user WHERE id=?")
        ps.setInt(1, id)
        val rs: ResultSet = ps.executeQuery()
        if (rs.next()) {
            return User(rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4))
        }
        return null
    }

    override fun getAll(): MutableList<User> {
        var users = mutableListOf<User>()
        val ps = DbConnection
                .conn
                .prepareStatement("SELECT * FROM app_user")
        val rs = ps.executeQuery()
        while (rs.next()) {
            var user = User(rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4))
            users.add(user)
        }
        return users
    }

    override fun add(obj: User) {
        val ps = DbConnection
                .conn
                .prepareStatement("INSERT INTO app_user (name,email,password) " +
                        "VALUES (?,?,?)")
        ps.setString(1, obj.name)
        ps.setString(2, obj.email)
        ps.setString(3, obj.password)
        ps.executeUpdate()
    }

    override fun add(objs: MutableList<User>) {
        val ps = DbConnection
                .conn
                .prepareStatement("INSERT INTO app_user (name,email,password) " +
                        "VALUES (?,?,?)")
        objs.iterator().forEach { obj ->
            run {
                ps.setString(1, obj.name)
                ps.setString(2, obj.email)
                ps.setString(3, obj.password)
                ps.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        val ps = DbConnection
                .conn
                .prepareStatement("DELETE FROM app_user " +
                        "WHERE id=?")
        ps.setInt(1, id)
        ps.executeUpdate()
    }

    override fun deleteAll() {
        val ps = DbConnection
                .conn
                .prepareStatement("DELETE FROM app_user")
        ps.executeUpdate()
    }
}