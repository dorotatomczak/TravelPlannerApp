package com.github.travelplannerapp.traveldetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.travelplannerapp.R
import com.github.travelplannerapp.accommodation.AccommodationActivity
import com.github.travelplannerapp.traveldialog.TravelDialog
import com.github.travelplannerapp.dayplans.DayPlansActivity
import com.github.travelplannerapp.tickets.TicketsActivity
import com.github.travelplannerapp.transport.TransportActivity
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import javax.inject.Inject

import kotlinx.android.synthetic.main.activity_travel_details.*
import kotlinx.android.synthetic.main.toolbar.*

class TravelDetailsActivity : AppCompatActivity(), TravelDetailsContract.View {

    @Inject
    lateinit var presenter: TravelDetailsContract.Presenter

    companion object {
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
        const val EXTRA_TRAVEL_NAME = "EXTRA_TRAVEL_NAME"
        const val REQUEST_TRAVEL_DETAILS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_details)

        setSupportActionBar(toolbar)
        buttonEditMode.visibility = View.VISIBLE
        buttonEditMode.setOnClickListener { showEditTravel() }

        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        recyclerViewTravelDetails.setHasFixedSize(true)
        recyclerViewTravelDetails.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewTravelDetails.adapter = TravelDetailsAdapter(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTravel()
    }

    override fun setTitle(title: String) {
        collapsing.title = title
    }

    override fun setResult(travelId: Int, travelName: String) {
        val resultIntent = Intent().apply {
            putExtra(EXTRA_TRAVEL_ID, travelId)
            putExtra(EXTRA_TRAVEL_NAME, travelName)
        }
        setResult(RESULT_OK, resultIntent)
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

    override fun showTickets(travelId: Int) {
        val intent = Intent(this, TicketsActivity::class.java)
        intent.putExtra(TicketsActivity.EXTRA_TRAVEL_ID, travelId)
        startActivity(intent)
    }

    override fun showEditTravel() {
        val editTravelDialog = TravelDialog(getString(R.string.change_travel_name))
        editTravelDialog.onOk = {
            val travelName = editTravelDialog.travelName.text.toString()
            presenter.changeTravelName(travelName)
        }
        editTravelDialog.show(supportFragmentManager, TravelDialog.TAG)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutTravelDetails, getString(messageCode), Snackbar.LENGTH_LONG).show()
    }
}
