package com.github.travelplannerapp.ServerApp.datamodels

class SearchResponse(val results: Results)

class Results(
    val previous: String,
    val next: String,
    val items: Array<Place>)
