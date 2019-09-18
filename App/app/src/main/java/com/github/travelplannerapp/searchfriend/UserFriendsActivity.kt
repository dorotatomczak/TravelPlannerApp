package com.github.travelplannerapp.searchfriend

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R



class UserFriendsActivity : AppCompatActivity() {
    private lateinit var friendsEmailList: Array<String>
    private lateinit var friendsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_friends)

        friendsListView = findViewById<ListView>(R.id.friendsList) as ListView
        friendsEmailList = arrayOf("alakot@wp.pl","zuzannakowalska@gmail.com", "aniakowal@o2.pl")

        val itemsAdapter = ArrayAdapter<String>(this,R.layout.item_one_of_list, friendsEmailList)
        friendsListView.adapter=itemsAdapter
    }
}
