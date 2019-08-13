package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TravelRepository : ITravelRepository {
    companion object {
        const val insertStatement = "INSERT INTO travel (name) VALUES (?)"
        const val selectStatement = "SELECT * FROM travel"
        const val deleteStatement = "DELETE FROM travel "
    }

    override fun getAllTravelsByUserId(id: Int): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT travel.id, travel.name " +
                                "FROM travel INNER JOIN app_user_travel " +
                                "on travel.id = app_user_travel.travel_id " +
                                "where app_user_travel.app_user_id = ?"
                )
        statement.setInt(1, id)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun getAllTravelsByUserEmail(email: String, authToken: String): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection.conn
                .prepareStatement(
                        "SELECT travel.id, travel.name " +
                                "FROM travel INNER JOIN app_user_travel " +
                                "ON travel.id = app_user_travel.travel_id " +
                                "INNER JOIN app_user " +
                                "ON app_user_travel.app_user_id = app_user.id " +
                                "WHERE app_user.email = ?"
                )
        statement.setString(1, email)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun get(id: Int): Travel? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE id=?")
        statement.setInt(1, id)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return Travel(result)
        }
        return null
    }

    override fun getAll(): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(Travel(result))
        }
        return travels
    }

    override fun add(obj: Travel) {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setString(1, obj.name)
        statement.executeUpdate()
    }

    override fun add(objs: MutableList<Travel>) {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        objs.iterator()
                .forEach { obj ->
                    run {
                        statement.setString(1, obj.name)
                        statement.executeUpdate()
                    }
                }
    }

    override fun delete(id: Int) {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement + "WHERE id=?")
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    override fun deleteAll() {
        val statement = DbConnection
                .conn
                .prepareStatement(deleteStatement)
        statement.executeUpdate()
    }
}