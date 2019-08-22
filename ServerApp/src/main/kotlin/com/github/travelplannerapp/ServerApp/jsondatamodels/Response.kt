package com.github.travelplannerapp.ServerApp.jsondatamodels

data class Response<T>(
        var statusCode: Int = 0,
        var data: T? = null
)