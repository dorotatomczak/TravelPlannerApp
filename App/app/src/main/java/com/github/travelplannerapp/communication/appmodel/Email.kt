package com.github.travelplannerapp.communication.appmodel

import android.app.SearchManager
import android.database.Cursor

data class Email(
        val value: String

) {
    constructor(c: Cursor) : this(
            c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)))
}

