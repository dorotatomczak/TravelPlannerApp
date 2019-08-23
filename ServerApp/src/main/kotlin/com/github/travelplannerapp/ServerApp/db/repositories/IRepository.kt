package com.github.travelplannerapp.ServerApp.db.repositories

interface IRepository<T> {
    fun get(id: Int): T?
    fun getAll(): MutableList<T>

    fun add(obj: T): Boolean

    fun update(obj: T): Boolean

    fun delete(id: Int): Boolean
    fun deleteAll(): Boolean

    fun getNextId(): Int
}