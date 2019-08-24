package com.github.travelplannerapp.ServerApp.db.repositories

import java.sql.PreparedStatement
import java.sql.ResultSet

interface IRepository<T> {
    fun get(id: Int): T?
    fun getAll(): MutableList<T>

    fun add(obj: T): Boolean

    fun update(obj: T): Boolean

    fun delete(id: Int): Boolean
    fun deleteAll(): Boolean

    fun getNextId(): Int

    fun T(result: ResultSet): T?

    fun prepareSelectByIdStatement(id: Int): PreparedStatement
    fun prepareSelectAllStatement(): PreparedStatement
    fun prepareInsertStatement(obj: T): PreparedStatement
    fun prepareUpdateStatement(obj: T): PreparedStatement
    fun prepareDeleteByIdStatement(id: Int): PreparedStatement
    fun prepareDeleteAllStatement(): PreparedStatement
    fun prepareNextIdStatement(): PreparedStatement
}