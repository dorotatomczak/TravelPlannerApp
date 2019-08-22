package com.github.travelplannerapp.utils

import java.security.MessageDigest

class PasswordUtils {

    fun hashPassword(password: String): String? {
        return MessageDigest.getInstance("SHA-512")
                .digest(password.toByteArray())
                .joinToString("", limit = 40) { Integer.toHexString(it.toInt()) }
    }
}