package com.github.travelplannerapp.ServerApp.services.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.Contacts
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.Place
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.Routes
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.SearchObjectsResponse
import com.github.travelplannerapp.ServerApp.datamodels.servermodel.CityObject

interface ISearchService {
    fun getExampleDataFromHere()
    fun getRoutes(
        startCoordinates: Pair<String, String>,
        destinationCoordinates: Pair<String, String>,
        transportMode: String,
        departureTime: String
    ): Routes

    fun getObjects(
        category: String,
        westSouthPoint: Pair<String, String>,
        eastNorthPoint: Pair<String, String>
    ): Array<Place>

    fun getPage(request: String): SearchObjectsResponse
    fun getCities(query: String): Array<CityObject>
    fun getContacts(href: String): Contacts
}
