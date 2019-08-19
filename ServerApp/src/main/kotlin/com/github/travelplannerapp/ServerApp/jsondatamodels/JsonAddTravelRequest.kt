package com.github.travelplannerapp.ServerApp.jsondatamodels

class JsonAddTravelRequest(
        var userId: Int,
        var auth: String,
        var travelName: String
)