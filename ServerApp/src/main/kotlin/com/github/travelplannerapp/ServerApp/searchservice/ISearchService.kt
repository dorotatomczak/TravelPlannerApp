package com.github.travelplannerapp.ServerApp.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.SearchResponse

interface ISearchService {
    fun getExampleDataFromHere()
    fun getObjects(category: String, westSouthPoint: Pair<String,String>, eastNorthPoint: Pair<String,String>) : SearchResponse
    fun getPage(request: String) : SearchResponse
}
