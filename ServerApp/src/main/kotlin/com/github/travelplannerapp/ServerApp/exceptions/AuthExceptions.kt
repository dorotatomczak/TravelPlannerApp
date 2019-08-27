package com.github.travelplannerapp.ServerApp.exceptions

class AuthorizationException(override val message:String, override val code: Int = 11): Exception(message), ApiException

class EmailAlreadyExistsException(override val message:String, override val code: Int = 12): Exception(message), ApiException

class WrongCredentialsException(override val message:String, override val code: Int = 13): Exception(message), ApiException