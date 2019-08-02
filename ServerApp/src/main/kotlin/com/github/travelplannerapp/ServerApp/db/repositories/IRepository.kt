package com.github.travelplannerapp.ServerApp.db.repositories

import java.sql.ResultSet

interface IRepository<T> {
    fun get(id: Int): T?
    fun getAll(): MutableList<T>

    fun add(obj: T)
    fun add(objs: MutableList<T>)

    fun delete(id: Int)
    fun deleteAll()

    fun mapResultToObject(result: ResultSet): T
}