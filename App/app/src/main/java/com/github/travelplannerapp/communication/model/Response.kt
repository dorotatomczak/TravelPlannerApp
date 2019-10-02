package com.github.travelplannerapp.communication.model

data class Response<T>(
        var responseCode: ResponseCode,
        var data: T? = null
)
