package com.github.travelplannerapp.ServerApp.datamodels

class SearchObjectsResponse(val results: Results)

class Results(
    val previous: String,
    val next: String,
    val items: Array<Place>)
