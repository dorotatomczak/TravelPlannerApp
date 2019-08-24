package com.github.travelplannerapp.ServerApp.db.repositories

abstract class Repository<T> : IRepository<T> {
    override fun get(id: Int): T? {
        val statement = prepareSelectByIdStatement(id)
        val result = statement.executeQuery()
        if (result.next()) {
            return T(result)
        }
        return null
    }

    override fun getAll(): MutableList<T> {
        val objs = mutableListOf<T>()
        val statement = prepareSelectAllStatement()
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
        val statement = prepareDeleteByIdStatement(id)
        return statement.executeUpdate() > 0
    }

    override fun deleteAll(): Boolean {
        val statement = prepareDeleteAllStatement()
        return statement.executeUpdate() > 0    }

    override fun getNextId(): Int {
        val statement = prepareNextIdStatement()
        val result = statement.executeQuery()
        result.next()
        return result.getInt("new_id")    }

}