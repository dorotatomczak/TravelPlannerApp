package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.communication.commonmodel.ResponseCode

class AddTravelException (override val message: String,
                          override val code: ResponseCode = ResponseCode.ADD_TRAVEL_ERROR): Exception(message), ApiException

class UpdateTravelException (override val message: String,
                             override val code: ResponseCode = ResponseCode.UPDATE_TRAVEL_ERROR): Exception(message), ApiException

class DeleteTravelsException (override val message: String,
                              override val code: ResponseCode = ResponseCode.DELETE_TRAVELS_ERROR): Exception(message), ApiException

class ShareTravelException(override val message: String,
                           override val code: ResponseCode = ResponseCode.SHARE_TRAVEL_ERROR) : Exception(message), ApiException
