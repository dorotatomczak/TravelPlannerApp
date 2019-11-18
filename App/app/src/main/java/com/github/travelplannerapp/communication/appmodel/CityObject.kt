package com.github.travelplannerapp.communication.appmodel

import android.app.SearchManager
import android.database.Cursor

class CityObject(
        val name: String,
        val country: String,
        val y: String,
        val x: String
) {
    constructor(c: Cursor) : this(
            c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)),
            c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)),
            c.getString(c.getColumnIndex("x")),
            c.getString(c.getColumnIndex("y")))
}
