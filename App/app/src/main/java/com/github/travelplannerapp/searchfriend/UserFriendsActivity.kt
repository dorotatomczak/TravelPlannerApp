package com.github.travelplannerapp.searchfriend

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class UserFriendsActivity : AppCompatActivity() {
    private lateinit var friendsListView: ListView
    private val compositeDisposable = CompositeDisposable()
    private var userFriends: UserFriends = UserFriends()
    private var friends = arrayListOf<String>()

    private fun loadFriends() {
        compositeDisposable.add(CommunicationService.serverApi.getUserFriends()
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map {
                    if
                            (it.responseCode == ResponseCode.OK) it.data!!
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
//        userFriends.friendsList.addAll(friends)
//        var selectedUser = intent.getStringExtra("selectedUser")
//        if (selectedUser !== null) {
//            userFriends.friendsList.add(selectedUser)
//        }
        val itemsAdapter = ArrayAdapter<String>(this, R.layout.item_one_of_list, friends)//userFriends.friendsList)
        friendsListView.adapter = itemsAdapter
    }

    private fun handleErrorResponse(error: Throwable) {
        //TO DO
        println(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_friends)

        friendsListView = findViewById<ListView>(R.id.friendsList) as ListView

        loadFriends()
    }
}
