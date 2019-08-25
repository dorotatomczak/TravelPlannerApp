package com.github.travelplannerapp.ServerApp.exceptions

class AddScanException(message:String, override val code: Int = 41): Exception(message), ApiException