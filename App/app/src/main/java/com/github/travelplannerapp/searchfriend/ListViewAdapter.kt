package com.github.travelplannerapp.searchfriend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.travelplannerapp.R

class ListViewAdapter(context: Context, usersEmails: ArrayList<UserEmail>)
    : ArrayAdapter<UserEmail>(context, 0, usersEmails) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val userEmail = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_email, parent, false)
        }
        val email = convertView!!.findViewById<View>(R.id.name) as TextView
        email.text = userEmail!!.userEmail
        return convertView
    }
}
