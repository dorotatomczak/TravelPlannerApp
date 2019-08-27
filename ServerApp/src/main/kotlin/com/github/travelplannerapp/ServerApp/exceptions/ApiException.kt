package com.github.travelplannerapp.ServerApp.exceptions

interface ApiException {
    val code: Int
    val message: String
}