package com.github.travelplannerapp.userfriends

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.utils.DrawerUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.toolbar.*

class UserFriendsActivity : AppCompatActivity() {
    private lateinit var friendsListView: ListView
    private val compositeDisposable = CompositeDisposable()
    private var friends = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_friends)
        friendsListView = findViewById<ListView>(R.id.friendsList) as ListView
        addToolbar()
        loadFriends()

        var notification = intent.getStringExtra("notification")
        if (notification != null)
            Snackbar.make(friendsListView, notification, Snackbar.LENGTH_LONG).show()

        friendsListView.setOnItemClickListener { _, _, position, _ ->
            var email = friends[position]
            val friendText = getString(R.string.friend)
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_one_entry, friendText))
                    .setMessage(getString(R.string.delete_one_confirmation, friendText))
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        deleteFriendByEmail(email)
                    }
                    .setNegativeButton(android.R.string.no) { _, _ ->
                    }
                    .show()

        }
    }

    private fun addToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)
    }

    private fun loadFriends() {
        compositeDisposable.add(CommunicationService.serverApi.getFriends(SharedPreferencesUtils.getUserId())
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map {
                    if (it.responseCode == ResponseCode.OK)
                        it.data!!
                    else
                        throw ApiException(it.responseCode)
                }
                .subscribe(
                        { friends -> handleLoadFriendsEmailsResponse(friends) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadFriendsEmailsResponse(userfriends: List<String>) {
        friends = ArrayList(userfriends)
        val itemsAdapter = ArrayAdapter<String>(this, R.layout.item_one_of_list, friends)
        friendsListView.adapter = itemsAdapter
    }

    private fun handleErrorResponse(error: Throwable) {
        Snackbar.make(friendsListView, R.string.error, Snackbar.LENGTH_LONG).show()
    }

    private fun deleteFriendByEmail(email: String) {
//        compositeDisposable.add(CommunicationService.serverApi.deleteFriend(email)
//                .observeOn(SchedulerProvider.ui())
//                .subscribeOn(SchedulerProvider.io())
//                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
//                .subscribe(
//                        { isTrue -> handleDeleteFriendResponse() },
//                        { error -> handleErrorResponse(error) }
//                ))
    }

    private fun handleDeleteFriendResponse() {
        val intent = Intent(this, UserFriendsActivity::class.java)
        intent.putExtra("notification", getString(R.string.friend_deleted))
        this.startActivity(intent)
    }
}
