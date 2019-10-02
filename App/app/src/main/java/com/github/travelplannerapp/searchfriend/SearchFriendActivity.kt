package com.github.travelplannerapp.searchfriend

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.userfriends.UserFriendsActivity
import com.github.travelplannerapp.utils.DrawerUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.ArrayList
import com.github.travelplannerapp.utils.SharedPreferencesUtils

class SearchFriendActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var usersListView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var allArrayList: ArrayList<String>
    private lateinit var arrayList: ArrayList<String>
    private lateinit var emails: MutableList<String>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friend)
        usersListView = findViewById<ListView>(R.id.usersList)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        loadUsersEmails()
        addSearchAbility()
        usersListView.setOnItemClickListener { _, _, position, _ ->
            var email = arrayList[position]
            val userText = getString(R.string.user)
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_entry, userText))
                    .setMessage(getString(R.string.add_one_confirmation, userText))
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        addFriend(email)
                    }
                    .setNegativeButton(android.R.string.no) { _, _ ->
                    }
                    .show()

        }
    }


    private fun loadUsersEmails() {
        emails = arrayListOf<String>()
        compositeDisposable.add(CommunicationService.serverApi.getUsersEmails()
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map {
                    if (it.responseCode == ResponseCode.OK)
                        it.data!!
                    else
                        throw ApiException(it.responseCode)
                }
                .subscribe(
                        { emails -> handleLoadEmailsResponse(emails) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadEmailsResponse(usersEmails: List<String>) {
        emails = ArrayList(usersEmails)
        createUsersEmailsList()
        adapter=ArrayAdapter<String>(this, R.layout.item_one_of_list, arrayList)
        usersListView.adapter = adapter
    }

    private fun createUsersEmailsList() {
        arrayList = ArrayList<String>()
        for (x in 0 until emails.size) {
            arrayList.add( emails[x])
        }
        allArrayList = arrayList.clone() as ArrayList<String>
    }

    private fun handleErrorResponse(error: Throwable) {
        Snackbar.make(usersListView, R.string.error, Snackbar.LENGTH_LONG).show()
    }

    private fun addSearchAbility() {
        var userSearchView: SearchView = findViewById(R.id.search)
        userSearchView.setOnQueryTextListener(this)

    }

    override fun onQueryTextChange(queryText: String): Boolean {
        var text: String = queryText
        filter(text)
        return false
    }

    override fun onQueryTextSubmit(queryText: String?): Boolean {
        var text: String = queryText.toString()
        filter(text)
        return true
    }

    private fun filter(text: String) {
        var charText = text.toLowerCase(Locale.getDefault())
        adapter.clear()
        if (charText.length !== 0)
            for (email in allArrayList) {
                if (email.toLowerCase(Locale.getDefault()).contains(charText))
                    adapter.add(email)
            }
        else adapter.addAll(allArrayList)
    }

    private fun addFriend(email: String) {
        compositeDisposable.add(CommunicationService.serverApi.addFriend(SharedPreferencesUtils.getUserId(),email)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { isTrue -> handleAddFriendResponse() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleAddFriendResponse() {
        val intent = Intent(this, UserFriendsActivity::class.java)
        intent.putExtra("notification", getString(R.string.friend_added))
        this.startActivity(intent)
    }
}
