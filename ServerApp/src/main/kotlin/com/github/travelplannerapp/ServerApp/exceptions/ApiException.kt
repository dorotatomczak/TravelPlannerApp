package com.github.travelplannerapp.ServerApp.exceptions

interface ApiException {
    val code: ResponseCode
    val message: String
}