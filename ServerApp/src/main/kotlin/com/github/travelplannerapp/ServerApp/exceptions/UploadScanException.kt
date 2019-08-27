package com.github.travelplannerapp.ServerApp.exceptions

class UploadScanException (message:String, override val code: Int = 31): Exception(message), ApiException