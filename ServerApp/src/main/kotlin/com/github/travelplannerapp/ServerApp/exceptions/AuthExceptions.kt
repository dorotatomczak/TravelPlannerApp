package com.github.travelplannerapp.ServerApp.exceptions

import com.github.travelplannerapp.communication.commonmodel.ResponseCode

class AuthorizationException(override val message: String,
                             override val code: ResponseCode = ResponseCode.AUTHORIZATION_ERROR) : Exception(message), ApiException

class EmailAlreadyExistsException(override val message: String,
                                  override val code: ResponseCode = ResponseCode.EMAIL_TAKEN_ERROR) : Exception(message), ApiException

class WrongCredentialsException(override val message: String,
                                override val code: ResponseCode = ResponseCode.AUTHENTICATION_ERROR) : Exception(message), ApiException
