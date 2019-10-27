package com.github.travelplannerapp.searchfriend

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.SharedPreferencesUtils

class SearchFriendSuggestionProvider : ContentProvider() {
    override fun query(query: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        val searchString = query.lastPathSegment.toString()
        return createCursor(searchString)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun getType(p0: Uri): String? {
        return "null"
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    private fun createCursor(searchString: String): MatrixCursor {
        val cursor = MatrixCursor(arrayOf("_id", SearchManager.SUGGEST_COLUMN_TEXT_1))

        var list = CommunicationService.serverApi.findMatchingEmails(searchString)

        list.blockingGet().data.orEmpty().forEach {
            cursor.addRow(arrayOf(it.id, it.email))
        }
        return cursor
    }
}
