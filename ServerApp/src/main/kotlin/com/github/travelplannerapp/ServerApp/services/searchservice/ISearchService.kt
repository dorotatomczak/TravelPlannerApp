package com.github.travelplannerapp.ServerApp.services.searchservice

import com.github.travelplannerapp.ServerApp.datamodels.servermodel.CityObject
import com.github.travelplannerapp.communication.commonmodel.Contacts
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.SearchObjectsResponse

interface ISearchService {
    fun getExampleDataFromHere()
    fun getTransport(
        startCoordinates: Pair<String, String>,
        destinationCoordinates: Pair<String, String>,
        transportMode: String
    ): String

    fun getObjects(
        category: String,
        westSouthPoint: Pair<String, String>,
        eastNorthPoint: Pair<String, String>
    ): Array<Place>

    fun getPage(request: String): SearchObjectsResponse
    fun findCities(query: String): Array<CityObject>
    fun getContacts(href: String): Contacts
}
