package com.github.travelplannerapp.util

class PasswordUtils {

    fun hashPassword(password: String): String? {
        return password.hashCode().toString()
    }
}