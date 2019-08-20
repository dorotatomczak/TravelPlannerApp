package com.github.travelplannerapp.communication.model

import java.io.Serializable

class Place(
        private val id: String,
        val title: String,
        private val vicinity: String,
        private val position: Array<Double>,
        private val category: ObjectCategory,
        private val categoryTitle: String,
        private val href: String,
        private val resultType: String,
        private val averageRating: String,
        private val distance: Double
) : Serializable {
}

class ObjectCategory(
        private val id: String,
        private val title: String,
        private val href: String,
        private val type: String,
        private val system: String
)
