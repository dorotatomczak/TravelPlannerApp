package com.github.travelplannerapp.initializers

import android.content.Context
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import javax.inject.Inject

class SharedPreferencesInitializer @Inject constructor(private val context: Context) : Initializer {
    override fun initialize() {
        SharedPreferencesUtils.initialize(context)
    }
}