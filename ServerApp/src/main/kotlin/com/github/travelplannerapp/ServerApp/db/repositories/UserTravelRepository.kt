package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.UserTravel
import java.sql.ResultSet

class UserTravelRepository : IUserTravelRepository {
    companion object {
        const val insertStatement = "INSERT INTO app_user_travel (app_user_id, travel_id) VALUES (?, ?) "
        const val selectStatement = "SELECT * FROM app_user_travel "
        const val deleteStatement = "DELETE FROM app_user_travel "
    }

    override fun get(id: Int): UserTravel? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE id=?")
        statement.setInt(1, id)
        val result: ResultSet = statement.executeQuery()
        if (result.next()) {
            return UserTravel(result)
        }
        return null
    }

    override fun getAll(): MutableList<UserTravel> {
        var userTravels = mutableListOf<UserTravel>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            userTravels.add(UserTravel(result))
        }
        return userTravels
    }

    override fun add(obj: UserTravel): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.userId)
        statement.setInt(2, obj.travelId)
        return statement.executeUpdate() > 0
    }

    override fun add(objs: MutableList<UserTravel>) {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        objs.iterator().forEach { obj ->
            run {
                statement.setInt(1, obj.userId)
                statement.setInt(2, obj.travelId)
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
}