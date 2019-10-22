package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.communication.commonmodel.ResponseCode

class RatePlaceException(
    override val message: String,
    override val code: ResponseCode = ResponseCode.RATE_PLACE_ERROR
) : Exception(message), ApiException
