package com.github.travelplannerapp.ServerApp.exceptions

class AddTravelException (override val message:String, override val code: Int = 21): Exception(message), ApiException