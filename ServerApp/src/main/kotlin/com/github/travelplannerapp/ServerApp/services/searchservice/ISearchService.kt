package com.github.travelplannerapp.ServerApp.services.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.CityObject
import com.github.travelplannerapp.ServerApp.datamodels.Place
import com.github.travelplannerapp.ServerApp.datamodels.SearchObjectsResponse

interface ISearchService {
    fun getExampleDataFromHere()
    fun getObjects(category: String, westSouthPoint: Pair<String,String>, eastNorthPoint: Pair<String,String>) : Array<Place>
    fun getPage(request: String) : SearchObjectsResponse
    fun findCities(query: String): Array<CityObject>
}
