package com.github.travelplannerapp.utils

import android.view.View
import android.view.inputmethod.InputMethodManager

object ActivityUtils {
    fun hideKeyboard(inputManager: InputMethodManager, currentFocus: View?) {
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
    }
}
