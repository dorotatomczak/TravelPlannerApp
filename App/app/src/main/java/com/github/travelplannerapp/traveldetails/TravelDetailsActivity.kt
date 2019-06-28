package com.github.travelplannerapp.traveldetails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.Toast
import com.github.travelplannerapp.R
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
        Toast.makeText(this, getString(R.string.day_plans), Toast.LENGTH_SHORT).show()
    }

    override fun showTransport() {
        Toast.makeText(this, getString(R.string.transport), Toast.LENGTH_SHORT).show()
    }

    override fun showAccommodation() {
        Toast.makeText(this, getString(R.string.accommodation), Toast.LENGTH_SHORT).show()
    }

    override fun showTickets() {
        Toast.makeText(this, getString(R.string.tickets), Toast.LENGTH_SHORT).show()
    }
}
