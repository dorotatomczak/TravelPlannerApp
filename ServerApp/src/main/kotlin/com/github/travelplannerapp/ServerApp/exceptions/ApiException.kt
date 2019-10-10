package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.communication.commonmodel.ResponseCode

interface ApiException {
    val code: ResponseCode
    val message: String
}
