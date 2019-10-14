package com.github.travelplannerapp.communication.commonmodel

data class Response<T>(
        var responseCode: ResponseCode,
        var data: T? = null
)
