package com.github.travelplannerapp.searchfriend

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.appmodel.UserInfo
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
        supportActionBar?.title = getString(R.string.my_friends)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        swipeRefreshLayoutFriends.setOnRefreshListener { presenter.loadFriends() }
        recyclerViewFriend.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewFriend.adapter = FriendAdapter(presenter)

        addSearchAbility();
        presenter.loadFriends()
    }

    override fun onDataSetChanged() {
        recyclerViewFriend.adapter?.notifyDataSetChanged()
    }

    override fun showActionMode() {
        searchViewFriend.visibility = View.GONE
    }

    override fun showNoActionMode() {
        searchViewFriend.visibility = View.VISIBLE
        (recyclerViewFriend.adapter as FriendAdapter).leaveActionMode()
    }

    override fun showConfirmationDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_friends))
                .setMessage(getString(R.string.delete_friends_confirmation))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    presenter.deleteFriends()
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                }
                .show()
    }

    override fun showFriends() {
        recyclerViewFriend.visibility = View.VISIBLE
    }

    override fun setLoadingIndicatorVisibility(isVisible: Boolean) {
        swipeRefreshLayoutFriends.isRefreshing = isVisible
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(linearLayoutSearchFriend, messageCode, Snackbar.LENGTH_LONG).show()
    }

    private fun showAddFriendConfirmationDialog(friend: UserInfo) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_friend))
                .setMessage(getString(R.string.add_user_confirmation))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    presenter.addFriend(friend)
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                }
                .show()
    }

    private fun closeKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
    }

    private fun addSearchAbility() {
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
                    val friend = UserInfo(suggestionsAdapter.getItem(position) as Cursor)
                    showAddFriendConfirmationDialog(friend)
                    return true
                }
            })
        }
    }
}


