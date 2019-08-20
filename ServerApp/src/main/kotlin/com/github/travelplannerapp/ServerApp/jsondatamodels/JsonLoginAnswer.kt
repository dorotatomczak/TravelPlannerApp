package com.github.travelplannerapp.ServerApp.jsondatamodels

class JsonLoginAnswer(
    var authorizationToken: String,
    var userId: Int,
    var result: LOGIN_ANSWER
)

enum class LOGIN_ANSWER{
    OK,
    ERROR
}