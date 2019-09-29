package com.github.travelplannerapp.searchfriend

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import kotlin.collections.ArrayList

private var friends = ArrayList<String>()

class SearchFriendActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var usersListView: ListView
    private lateinit var adapter: ListViewAdapter
    private lateinit var userSearch: SearchView
    
    private lateinit var arrayList: ArrayList<UserEmail>
    private lateinit var allArrayList: ArrayList<UserEmail>
    private lateinit var emails: MutableList<String>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friend)
        emails = arrayListOf<String>()
        usersListView = findViewById<ListView>(R.id.usersList)
        loadUsersEmails()

        userSearch = findViewById(R.id.search)
        userSearch.setOnQueryTextListener(this)

        usersListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = arrayList[position]
            val intent = Intent(this, UserFriendsActivity::class.java)
            intent.putExtra("selectedUser", selectedUser.userEmail)
            intent.putExtra("friends", friends)
            this.startActivity(intent)
        }
    }

    override fun onQueryTextChange(queryText: String): Boolean {
        var text: String = queryText
        filter(text)
        return false
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
        //TO_DO
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
    private fun loadUsersEmails() {

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
                        { emails ->
                            handleLoadEmailsResponse(emails)
                        },
                        { error ->
                            handleErrorResponse(error)
                        }
                ))
    }
    private fun handleLoadEmailsResponse(usersEmails: List<String>) {
        emails = ArrayList(usersEmails)
        createUsersEmailsList()
        adapter = ListViewAdapter(this, arrayList)
        usersListView.adapter = adapter
    }

    private fun handleErrorResponse(error: Throwable) {
        //TO DO
        println(error)
    }
    private fun createUsersEmailsList(){
        arrayList = ArrayList<UserEmail>()
        for (x in 0 until emails.size) {
            var userEmail: UserEmail = UserEmail(emails[x])
            arrayList.add(userEmail)
        }
        allArrayList = arrayList.clone() as ArrayList<UserEmail>
    }
}
