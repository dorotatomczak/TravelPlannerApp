package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Travel
import org.springframework.stereotype.Component

@Component
class TravelRepository : ITravelRepository {
    companion object {
        private const val insertStatementWithId = "INSERT INTO travel (id, name) VALUES (?, ?) "
        private const val insertStatement = "INSERT INTO travel (name) VALUES (?) "
        private const val selectStatement = "SELECT * FROM travel "
        private const val deleteStatement = "DELETE FROM travel "
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

    override fun getAllTravelsByUserEmail(email: String): MutableList<Travel> {
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
        val result = statement.executeQuery()
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

    override fun add(obj: Travel): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatementWithId)
        statement.setInt(1, obj.id)
        statement.setString(2, obj.name)
        return statement.executeUpdate() > 0
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
        var id = -1
        while (result.next()) {
            id = result.getInt(1)
        }
        return id
    }
}