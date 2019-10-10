package com.github.travelplannerapp.ServerApp.datamodels.commonmodel

import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.Place

class SearchObjectsResponse(val results: Results)

class Results(
        val previous: String,
        val next: String,
        val items: Array<Place>)
