package com.github.travelplannerapp.travels

import android.content.Context
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
import com.github.travelplannerapp.addtravel.AddTravelDialog
import com.github.travelplannerapp.communication.ServerApi
import com.github.travelplannerapp.jsondatamodels.ADD_TRAVEL_ANSWER
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_travels.*

class TravelsActivity : AppCompatActivity(), TravelsContract.View, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var presenter: TravelsContract.Presenter
    private var myCompositeDisposable: CompositeDisposable? = null
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)
        myCompositeDisposable = CompositeDisposable()

        setSupportActionBar(toolbarTravels)
        supportActionBar?.setHomeButtonEnabled(true)

        toggle = ActionBarDrawerToggle(this, drawerLayoutTravels, toolbarTravels, R.string.drawer_open, R.string.drawer_close)
        drawerLayoutTravels.addDrawerListener(toggle)
        toggle.syncState()

        navigationViewTravels.setNavigationItemSelectedListener(this)

        fabTravels.setOnClickListener {
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
        myCompositeDisposable?.clear()
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
        val dialog = AddTravelDialog()
        dialog.onOk = {
            val sharedPref = getSharedPreferences(resources.getString(R.string.auth_settings),
                    Context.MODE_PRIVATE)
            val email = sharedPref.getString(resources.getString(R.string.email_shared_pref),
                    "default").toString()
            val authToken = sharedPref.getString(resources.getString(R.string.auth_token_shared_pref),
                    "default").toString()
            val travelName = dialog.travelName.text.toString()
            presenter.addTravel(email, authToken, travelName)
        }
        dialog.show(supportFragmentManager, "Add travel dialog")
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

    override fun showTravels() {
        textViewNoTravels.visibility = View.GONE
        recyclerViewTravels.visibility = View.VISIBLE
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayoutTravels, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    override fun showAddTravelResult(result: ADD_TRAVEL_ANSWER) {
        when (result) {
            // TODO [Magda] refresh travels list
            ADD_TRAVEL_ANSWER.OK -> showSnackbar(resources.getString(R.string.add_travel_ok))
            ADD_TRAVEL_ANSWER.ERROR -> showSnackbar(resources.getString(R.string.add_travel_error))
        }
    }

    override fun loadTravels(requestInterface: ServerApi, handleResponse: (myTravels: List<String>) -> Unit) {
        val sharedPref = getSharedPreferences(resources.getString(R.string.auth_settings),
                Context.MODE_PRIVATE)
        val email = sharedPref.getString(resources.getString(R.string.email_shared_pref),
                "default").toString()
        val authToken = sharedPref.getString(resources.getString(R.string.auth_token_shared_pref),
                "default").toString()

        myCompositeDisposable?.add(requestInterface.getTravels(email, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(handleResponse, { showSnackbar(resources.getString(R.string.server_connection_failure)) }))
    }

    override fun addTravel(requestInterface: ServerApi, jsonAddTravelRequest: String,
                           handleAddTravelResponse: (jsonString: String) -> Unit) {
        myCompositeDisposable?.add(requestInterface.addTravel(jsonAddTravelRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(handleAddTravelResponse, { showSnackbar(resources.getString(R.string.server_connection_failure)) }))
    }
}
