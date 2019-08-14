package com.github.travelplannerapp.utils


class PasswordUtils {

    fun hashPassword(password: String): String? {
        return password.hashCode().toString()
    }
}