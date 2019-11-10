package com.github.travelplannerapp.communication

import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.ResponseCode

class ApiException(private val responseCode: ResponseCode) : Throwable() {

    fun getErrorMessageCode(): Int {
        return when (responseCode) {
            ResponseCode.AUTHORIZATION_ERROR -> R.string.authorization_error
            ResponseCode.EMAIL_TAKEN_ERROR -> R.string.sign_up_email_error
            ResponseCode.AUTHENTICATION_ERROR -> R.string.sign_in_error
            ResponseCode.ADD_TRAVEL_ERROR -> R.string.add_travel_error
            ResponseCode.UPDATE_TRAVEL_ERROR -> R.string.update_travel_error
            ResponseCode.DELETE_TRAVELS_ERROR -> R.string.delete_travels_error
            ResponseCode.SHARE_TRAVEL_ERROR -> R.string.share_travel_error
            ResponseCode.ADD_SCAN_ERROR -> R.string.add_scan_error
            ResponseCode.UPLOAD_SCAN_ERROR -> R.string.scan_upload_error
            ResponseCode.DELETE_SCANS_ERROR -> R.string.delete_scans_error
            ResponseCode.NO_ITEMS_SEARCH_ERROR -> R.string.no_items_search_error
            ResponseCode.ADD_PLAN_ELEMENT_ERROR -> R.string.add_plan_error
            ResponseCode.UPDATE_PLAN_ELEMENT_ERROR -> R.string.update_plan_error
            ResponseCode.DELETE_PLAN_ELEMENTS_ERROR -> R.string.delete_plan_elements_error
            ResponseCode.OTHER_ERROR -> R.string.try_again
            ResponseCode.ADD_FRIEND_ERROR -> R.string.add_friend_error
            ResponseCode.DELETE_FRIENDS_ERROR -> R.string.delete_friends_error
            ResponseCode.RATE_PLACE_ERROR -> R.string.rate_place_error
            else -> R.string.try_again
        }
    }
}
