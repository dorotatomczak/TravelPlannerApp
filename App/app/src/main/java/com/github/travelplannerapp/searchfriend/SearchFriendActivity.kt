package com.github.travelplannerapp.searchfriend

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.model.UserInfo
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search_friend.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class SearchFriendActivity : AppCompatActivity(), SearchFriendContract.View {

    @Inject
    lateinit var presenter: SearchFriendContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friend)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        swipeRefreshLayoutFriends.setOnRefreshListener { }
        recyclerViewFriend.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchViewFriend.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)

            setOnSuggestionListener(object : androidx.appcompat.widget.SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    closeKeyboard()
                    var c = suggestionsAdapter.getItem(position) as Cursor
                    val friend = UserInfo(suggestionsAdapter.getItem(position) as Cursor)
                    showAddFriend(friend)
                    return true
                }
            })
        }
    }

    override fun showAddFriend(friend: UserInfo) {
        val userText = getString(R.string.user)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_entry, userText))
                .setMessage(getString(R.string.add_one_confirmation, friend.email))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    presenter.addFriend(friend)
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                }
                .show()
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(linearLayoutSearchFriend, messageCode, Snackbar.LENGTH_LONG).show()
    }

    private fun closeKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
    }

}


