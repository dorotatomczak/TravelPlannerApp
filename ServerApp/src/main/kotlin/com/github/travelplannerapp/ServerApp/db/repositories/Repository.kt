package com.github.travelplannerapp.ServerApp.db.repositories

import com.github.travelplannerapp.ServerApp.db.DbConnection

abstract class Repository<T> : IRepository<T> {
    protected abstract val selectStatement: String
    protected abstract val insertStatement: String
    protected abstract val deleteStatement: String
    protected abstract val updateStatement: String
    protected abstract val nextIdStatement: String

    override fun get(id: Int): T? {
        val statement =DbConnection
                .conn
                .prepareStatement(selectStatement + "WHERE id=?")
        statement.setInt(1, id)
        val result = statement.executeQuery()
        if (result.next()) {
            return T(result)
        }
        return null
    }

    override fun getAll(): MutableList<T> {
        val objs = mutableListOf<T>()
        val statement = DbConnection
                .conn
                .prepareStatement(selectStatement)
        val result = statement.executeQuery()
        while (result.next()) {
            objs.add(T(result)!!)
        }
        return objs
    }

    override fun add(obj: T): Boolean {
        val statement = prepareInsertStatement(obj)
        return statement.executeUpdate() > 0
    }

    override fun update(obj: T): Boolean {
        val statement = prepareUpdateStatement(obj)
        return statement.executeUpdate() > 0    }

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
        return statement.executeUpdate() > 0    }

    override fun getNextId(): Int {
        val statement = DbConnection.conn
                .prepareStatement(nextIdStatement)
        val result = statement.executeQuery()
        result.next()
        return result.getInt("new_id")    }

}