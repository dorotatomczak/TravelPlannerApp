package com.github.travelplannerapp.ServerApp.exceptions

//TODO [Dorota] Another candidate for an external dependency
enum class ResponseCode {
    OK,
    AUTHORIZATION_ERROR,
    EMAIL_TAKEN_ERROR,
    AUTHENTICATION_ERROR,
    ADD_TRAVEL_ERROR,
    ADD_SCAN_ERROR,
    UPLOAD_SCAN_ERROR,
    OTHER_ERROR
}