package com.github.travelplannerapp.ServerApp.exceptions

class AddScanException(override val message: String,
                       override val code: ResponseCode = ResponseCode.ADD_SCAN_ERROR): Exception(message), ApiException

class UploadScanException (override val message: String,
                           override val code: ResponseCode = ResponseCode.UPLOAD_SCAN_ERROR): Exception(message), ApiException