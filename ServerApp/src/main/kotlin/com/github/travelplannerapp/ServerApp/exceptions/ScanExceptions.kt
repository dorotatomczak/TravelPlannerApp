package com.github.travelplannerapp.ServerApp.exceptions

class AddScanException(override val message:String, override val code: Int = 41): Exception(message), ApiException

class UploadScanException (override val message:String, override val code: Int = 31): Exception(message), ApiException