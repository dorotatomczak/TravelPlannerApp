package com.github.travelplannerapp.ServerApp.exceptions

class AddTravelException (message:String, override val code: Int = 21): Exception(message), ApiException