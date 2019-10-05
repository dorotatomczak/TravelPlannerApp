package com.github.travelplannerapp.utils

import java.security.MessageDigest

object PasswordUtils {

    fun hashPassword(password: String): String? {
        return MessageDigest.getInstance("SHA-256")
                .digest(password.toByteArray())
                .joinToString("", limit = 40) { Integer.toString(it.toInt()) }.substring(0, 20)
    }
}
