package com.github.travelplannerapp.ServerApp.datamodels

import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode

data class Response<T>(
        var responseCode: ResponseCode,
        var data: T? = null
)
