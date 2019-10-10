package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.ResponseCode

interface ApiException {
    val code: ResponseCode
    val message: String
}
