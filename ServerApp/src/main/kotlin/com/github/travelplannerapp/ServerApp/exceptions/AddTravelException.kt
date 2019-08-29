package com.github.travelplannerapp.ServerApp.exceptions

class AddTravelException(override val message: String,
                         override val code: ResponseCode = ResponseCode.ADD_TRAVEL_ERROR) : Exception(message), ApiException