package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TravelRepository : ITravelRepository {
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
            travels.add(mapResultToObject(result))
        }
        return travels
    }

    override fun getAllTravelsByUserName(name: String, authToken: String): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection.conn
                .prepareStatement(
                    "SELECT travel.id, travel.name " +
                    "FROM travel INNER JOIN app_user_travel " +
                    "ON travel.id = app_user_travel.travel_id " +
                    "INNER JOIN app_user " +
                    "ON app_user_travel.app_user_id = app_user.id " +
                    "WHERE app_user.name = ?"
                )
        statement.setString(1, name)
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(mapResultToObject(result))
        }
        return travels
    }

    override fun get(id: Int): Travel? {
        val statement = DbConnection
                .conn
                .prepareStatement("SELECT * FROM travel WHERE id=?")
        statement.setInt(1, id)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return mapResultToObject(result)
        }
        return null
    }

    override fun getAll(): MutableList<Travel> {
        var travels = mutableListOf<Travel>()
        val statement = DbConnection
                .conn
                .prepareStatement("SELECT * FROM travel")
        val result = statement.executeQuery()
        while (result.next()) {
            travels.add(mapResultToObject(result))
        }
        return travels
    }

    override fun add(obj: Travel) {
        val statement = DbConnection
                .conn
                .prepareStatement(
                    "INSERT INTO travel (name) " +
                    "VALUES (?)"
                )
        statement.setString(1, obj.name)
        statement.executeUpdate()
    }

    override fun add(objs: MutableList<Travel>) {
        val statement = DbConnection
                .conn
                .prepareStatement(
                    "INSERT INTO travel (name) " +
                    "VALUES (?)"
                )
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
                .prepareStatement(
                    "DELETE FROM travel " +
                    "WHERE id=?"
                )
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    override fun deleteAll() {
        val statement = DbConnection
                .conn
                .prepareStatement("DELETE FROM travel")
        statement.executeUpdate()
    }

    override fun mapResultToObject(result: ResultSet): Travel {
        return Travel(
            result.getInt(1),
            result.getString(2)
        )
    }
}