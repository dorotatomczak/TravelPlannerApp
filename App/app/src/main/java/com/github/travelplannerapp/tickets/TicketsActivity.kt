package com.github.travelplannerapp.tickets

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class TicketsActivity : AppCompatActivity(), TicketsContract.View {

    @Inject
    lateinit var presenter: TicketsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)
    }
}
