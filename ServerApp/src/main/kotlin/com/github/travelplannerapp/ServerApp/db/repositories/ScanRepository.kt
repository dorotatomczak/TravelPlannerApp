package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection
import com.github.travelplannerapp.ServerApp.db.dao.Scan
import org.springframework.stereotype.Component

@Component
class ScanRepository : IScanRepository {

    companion object {
        private const val insertStatement = "INSERT INTO scan (id, user_id, travel_id, name) VALUES (?, ?, ?, ?) "
        private const val selectStatement = "SELECT * FROM scan "
        private const val deleteStatement = "DELETE FROM scan "
    }

    override fun getAll(userId: Int, travelId: Int): MutableList<Scan> {
        val scans = mutableListOf<Scan>()
        val statement = DbConnection
                .conn
                .prepareStatement(
                        "SELECT scan.id, scan.user_id, scan.travel_id, scan.name " +
                                "FROM scan where scan.user_id = ? AND scan.travel_id = ?"
                )
        statement.setInt(1, userId)
        statement.setInt(2, travelId)
        val result = statement.executeQuery()
        while (result.next()) {
            scans.add(Scan(result))
        }
        return scans
    }

    override fun get(id: Int): Scan? {
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE id=?")
        statement.setInt(1, id)
        val result = statement.executeQuery()
        if (result.next()) {
            return Scan(result)
        }
        return null
    }

    override fun getAll(): MutableList<Scan> {
        val scans = mutableListOf<Scan>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            scans.add(Scan(result))
        }
        return scans
    }

    override fun add(obj: Scan): Boolean {
        val statement = DbConnection
                .conn
                .prepareStatement(insertStatement)
        statement.setInt(1, obj.id)
        statement.setInt(2, obj.userId)
        statement.setInt(3, obj.travelId)
        statement.setString(4, obj.name)
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
                        "SELECT nextval(pg_get_serial_sequence('scan', 'id')) AS new_id;"
                )
        val result = statement.executeQuery()
        result.next()
        return result.getInt("new_id")
    }

}