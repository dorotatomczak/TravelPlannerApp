package com.github.travelplannerapp.ServerApp.jsondatamodels

class JsonAddTravelAnswer(
        var result: ADD_TRAVEL_RESULT
)

enum class ADD_TRAVEL_RESULT {
    OK,
    ERROR
}