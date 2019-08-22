package com.github.travelplannerapp.ServerApp.exceptions

class AuthorizationException(message:String, override val code: Int = 11): Exception(message), ApiException

class EmailAlreadyExistsException(message:String, override val code: Int = 12): Exception(message), ApiException

class WrongCredentialsException(message:String, override val code: Int = 13): Exception(message), ApiException