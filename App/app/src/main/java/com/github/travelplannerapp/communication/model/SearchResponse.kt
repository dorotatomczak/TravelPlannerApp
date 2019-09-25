package com.github.travelplannerapp.communication.model

class SearchResponse(val results: Results)

class Results(
        val previous: String,
        val next: String,
        val items: Array<Place>)
