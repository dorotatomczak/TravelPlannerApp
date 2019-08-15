package com.github.travelplannerapp.ServerApp.jsondatamodels

class JsonLoginResponse(
    var authorizationToken: String,
    var result: LoginResponse
)

enum class LoginResponse{
    OK,
    ERROR
}