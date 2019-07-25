package com.github.travelplannerapp.travels

import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView

import com.github.travelplannerapp.R
import com.github.travelplannerapp.addtravel.AddTravelActivity
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_travels.*


class TravelsActivity : AppCompatActivity(), TravelsContract.View, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var presenter: TravelsContract.Presenter

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        setSupportActionBar(toolbarTravels)
        supportActionBar?.setHomeButtonEnabled(true)

        toggle = ActionBarDrawerToggle(this, drawerLayoutTravels, toolbarTravels, R.string.drawer_open, R.string.drawer_close)
        drawerLayoutTravels.addDrawerListener(toggle)
        toggle.syncState()

        navigationViewTravels.setNavigationItemSelectedListener(this)

        fabTravels.setOnClickListener {
            showAddTravel()
        }

        fabContactServer.setOnClickListener {
            presenter.contactServer()
        }

        recyclerViewTravels.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewTravels.adapter = TravelsAdapter(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTravels()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //TODO [Dorota] Showing snackbar is only temporary
        when (item.itemId) {
            R.id.menuMainSettings -> showSnackbar(getString(R.string.menu_settings))
            R.id.menuMainSignOut -> showSnackbar(getString(R.string.menu_sign_out))
        }
        return true
    }

    override fun showAddTravel() {
        val intent = Intent(this, AddTravelActivity::class.java)
        startActivity(intent)
    }

    override fun showTravelDetails(travel: String) {
        val intent = Intent(this, TravelDetailsActivity::class.java)
        intent.putExtra(TravelDetailsActivity.EXTRA_TRAVEL_ID, travel)
        startActivity(intent)
    }

    override fun showNoTravels() {
        textViewNoTravels.visibility = View.VISIBLE
        recyclerViewTravels.visibility = View.GONE
    }

    override fun showTravels(){
        textViewNoTravels.visibility = View.GONE
        recyclerViewTravels.visibility = View.VISIBLE
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayoutTravels, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }
}
