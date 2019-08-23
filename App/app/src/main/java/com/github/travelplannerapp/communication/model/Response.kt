package com.github.travelplannerapp.communication.model

data class Response<T>(
        var statusCode: Int = 0,
        var data: T? = null
)