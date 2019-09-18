package com.github.travelplannerapp.searchfriend

import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import java.util.*
import kotlin.collections.ArrayList

class SearchFriendActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var usersListView: ListView
    private lateinit var adapter: ListViewAdapter
    private lateinit var userSearch: SearchView
    private lateinit var userEmailList: Array<String>
    private lateinit var arrayList: ArrayList<UserEmail>
    private lateinit var allArrayList: ArrayList<UserEmail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friend)
        usersListView = findViewById<ListView>(R.id.usersList)

        getUsersEmail()

        allArrayList = arrayList.clone() as ArrayList<UserEmail>
        adapter = ListViewAdapter(this, arrayList)
        usersListView.adapter = adapter

        userSearch = findViewById(R.id.search)
        userSearch.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(queryText: String): Boolean {
        var text: String = queryText
        filter(text)
        return false
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    private fun getUsersEmail() {
        userEmailList = arrayOf("alakot@wp.pl", "arzymyszkiewicz@o2.pl", "blewandowska@gmail.com",
                "zuzannakowalska@gmail.com", "aniakowal@o2.pl", "aniapolak@o2.pl")
        arrayList = ArrayList<UserEmail>()
        for (x in 0 until userEmailList.size) {
            var userEmail: UserEmail = UserEmail(userEmailList[x])
            arrayList.add(userEmail)
        }
    }

    private fun filter(text: String) {
        var charText = text
        charText = charText.toLowerCase(Locale.getDefault())
        adapter.clear()
        if (charText.length === 0) {
            adapter.addAll(allArrayList)
        } else {
            for (wp in allArrayList) {
                if (wp.userEmail.toLowerCase(Locale.getDefault()).contains(charText)) {
                    adapter.add(wp)
                }
            }
        }
    }
}
