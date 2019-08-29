package com.github.travelplannerapp.ServerApp.jsondatamodels

import com.github.travelplannerapp.ServerApp.exceptions.ResponseCode

data class Response<T>(
        var responseCode: ResponseCode,
        var data: T? = null
)