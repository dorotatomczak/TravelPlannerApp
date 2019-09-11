package com.github.travelplannerapp.ServerApp.datamodels


//variables must be uppercase as they are in json response
class SearchCitiesResponse(
    val Res: Result
)

class Result(
    val Coverage: Coverage
)

class Coverage(
    val Cities: City
)

class City(
    val City: Array<CityObject>
)

class CityObject(
    val name: String,
    val country: String,
    val y: String,
    val x: String
)
