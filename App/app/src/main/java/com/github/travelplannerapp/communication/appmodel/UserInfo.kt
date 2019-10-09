package com.github.travelplannerapp.communication.appmodel

import android.app.SearchManager
import android.database.Cursor

data class UserInfo(
        val id: Int,
        val email: String

) {
    constructor(c: Cursor) : this(
            c.getInt(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)),
            c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)))
}
