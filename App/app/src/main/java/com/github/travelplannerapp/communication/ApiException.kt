package com.github.travelplannerapp.communication

import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.model.ResponseCode

class ApiException(private val responseCode: ResponseCode): Throwable() {

    fun getErrorMessageCode(): Int {
        return when (responseCode) {
            ResponseCode.AUTHORIZATION_ERROR -> R.string.authorization_error
            ResponseCode.EMAIL_TAKEN_ERROR -> R.string.sign_up_email_error
            ResponseCode.AUTHENTICATION_ERROR -> R.string.sign_in_error
            ResponseCode.ADD_TRAVEL_ERROR -> R.string.add_travel_error
            ResponseCode.ADD_SCAN_ERROR -> R.string.add_scan_error
            ResponseCode.UPLOAD_SCAN_ERROR -> R.string.scan_upload_error
            ResponseCode.OTHER_ERROR-> R.string.try_again
            else -> R.string.try_again
        }
    }
}