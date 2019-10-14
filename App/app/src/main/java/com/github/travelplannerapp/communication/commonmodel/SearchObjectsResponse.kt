package com.github.travelplannerapp.communication.commonmodel

class SearchObjectsResponse(val results: Results)

class Results(
        val previous: String,
        val next: String,
        val items: Array<Place>)
