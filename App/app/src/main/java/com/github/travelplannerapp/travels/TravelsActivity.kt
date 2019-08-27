package com.github.travelplannerapp.travels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity
import com.github.travelplannerapp.utils.DrawerUtils

import javax.inject.Inject

import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_travels.*
import kotlinx.android.synthetic.main.fab_add.*
import kotlinx.android.synthetic.main.toolbar.*
import com.github.travelplannerapp.R
import com.github.travelplannerapp.addtravel.AddTravelDialog
import com.github.travelplannerapp.utils.SharedPreferencesUtils

class TravelsActivity : AppCompatActivity(), TravelsContract.View {

    @Inject
    lateinit var presenter: TravelsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        fabAdd.setOnClickListener {
            showAddTravel()
        }

        recyclerViewTravels.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewTravels.adapter = TravelsAdapter(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTravels()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun showAddTravel() {
        val addTravelDialog = AddTravelDialog()
        addTravelDialog.onOk = {
            val travelName = addTravelDialog.travelName.text.toString()
            presenter.addTravel(
                    SharedPreferencesUtils.getUserId(),
                    SharedPreferencesUtils.getAccessToken()!!,
                    travelName
            )
        }
        addTravelDialog.show(supportFragmentManager, AddTravelDialog.TAG)
    }

    override fun showTravelDetails(travelId: Int, travelName: String) {
        val intent = Intent(this, TravelDetailsActivity::class.java)
        intent.putExtra(TravelDetailsActivity.EXTRA_TRAVEL_ID, travelId)
        intent.putExtra(TravelDetailsActivity.EXTRA_TRAVEL_NAME, travelName)
        startActivity(intent)
    }

    override fun showNoTravels() {
        textViewNoTravels.visibility = View.VISIBLE
        recyclerViewTravels.visibility = View.GONE
    }

    override fun showTravels() {
        textViewNoTravels.visibility = View.GONE
        recyclerViewTravels.visibility = View.VISIBLE
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutTravels, getString(messageCode), Snackbar.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayoutTravels, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDataSetChanged() {
        recyclerViewTravels.adapter?.notifyDataSetChanged()
    }

}
