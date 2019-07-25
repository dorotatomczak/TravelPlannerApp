package com.github.travelplannerapp.traveldetails

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.widget.Toast
import com.github.travelplannerapp.R
import com.github.travelplannerapp.accommodation.AccommodationActivity
import com.github.travelplannerapp.dayplans.DayPlansActivity
import com.github.travelplannerapp.tickets.TicketsActivity
import com.github.travelplannerapp.transport.TransportActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

import kotlinx.android.synthetic.main.activity_travel_details.*

class TravelDetailsActivity : AppCompatActivity(), TravelDetailsContract.View {

    @Inject
    lateinit var presenter: TravelDetailsContract.Presenter

    companion object {
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_details)

        setSupportActionBar(toolbarTravelDetails)

        recyclerViewTravelDetails.setHasFixedSize(true)
        recyclerViewTravelDetails.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewTravelDetails.adapter = TravelDetailsAdapter(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTravel()
    }

    override fun setTitle(title: String) {
        toolbarTravelDetails.title = title
    }

    override fun showDayPlans() {
        val intent = Intent(this, DayPlansActivity::class.java)
        startActivity(intent)
    }

    override fun showTransport() {
        val intent = Intent(this, TransportActivity::class.java)
        startActivity(intent)
    }

    override fun showAccommodation() {
        val intent = Intent(this, AccommodationActivity::class.java)
        startActivity(intent)
    }

    override fun showTickets() {
        val intent = Intent(this, TicketsActivity::class.java)
        startActivity(intent)
    }
}
