package com.github.travelplannerapp.ServerApp.datamodels.commonmodel

data class Response<T>(
        var responseCode: ResponseCode,
        var data: T? = null
)
