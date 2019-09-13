package com.github.travelplannerapp.communication.model

import java.io.Serializable

class Place(
        val id: String,
        val title: String,
        val vicinity: String,
        val position: Array<Double>,
        val category: ObjectCategory,
        val categoryTitle: String,
        val href: String,
        val resultType: String,
        val averageRating: String,
        val distance: Double
): Serializable

class ObjectCategory(
        private val id: String,
        private val title: String,
        private val href: String,
        private val type: String,
        private val system: String
)
