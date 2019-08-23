package com.github.travelplannerapp.communication

import com.github.travelplannerapp.R

class ApiException(private val statusCode: Int): Throwable() {

    fun getErrorMessageCode(): Int {
        return when (statusCode) {
            11 -> R.string.authorization_error
            12 -> R.string.sign_up_email_error
            13 -> R.string.sign_in_error
            21 -> R.string.add_travel_error
            31 -> R.string.scan_upload_failure
            else -> R.string.try_again
        }
    }
}