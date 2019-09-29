package com.github.travelplannerapp.searchfriend

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R


private var userFriends: UserFriends = UserFriends()

class UserFriendsActivity : AppCompatActivity() {
    private lateinit var friendsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_friends)

        friendsListView = findViewById<ListView>(R.id.friendsList) as ListView

        userFriends = UserFriends()
        var selectedUser = intent.getStringExtra("selectedUser")
        userFriends.friendsList.add(selectedUser)
        val itemsAdapter = ArrayAdapter<String>(this, R.layout.item_one_of_list, userFriends.friendsList)
        friendsListView.adapter = itemsAdapter
    }
}
