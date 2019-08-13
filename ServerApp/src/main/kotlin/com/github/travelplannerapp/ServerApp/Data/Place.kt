package com.github.travelplannerapp.ServerApp.Data

class Place(
        private val id: String, private val title: String, private val vicinity: String,
        private val position: Position, private val category: String, private val categoryTitle: String,
        private val href: String, private val resultType: String, private val distance: Double) {
    //TEMPORARY
    fun printPlace() {
        println("Place: ")
        println(this.id)
        println(this.title)
        println(this.vicinity)
        println(this.position)
        println(this.position)
        println(this.category)
        println(this.categoryTitle)
        println(this.href)
        println(this.resultType)
        println(this.distance)
    }
}