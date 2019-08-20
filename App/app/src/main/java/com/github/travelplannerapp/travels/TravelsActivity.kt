package com.github.travelplannerapp.travels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.communication.ServerApi
import com.github.travelplannerapp.jsondatamodels.ADD_TRAVEL_ANSWER
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity
import com.github.travelplannerapp.utils.DrawerUtils

import javax.inject.Inject

import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_travels.*
import kotlinx.android.synthetic.main.fab_add.*
import kotlinx.android.synthetic.main.toolbar.*
import com.github.travelplannerapp.R
import com.github.travelplannerapp.addtravel.AddTravelDialog
import com.github.travelplannerapp.utils.SharedPreferencesUtils

class TravelsActivity : AppCompatActivity(), TravelsContract.View {

    @Inject
    lateinit var presenter: TravelsContract.Presenter
    private var myCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)
        myCompositeDisposable = CompositeDisposable()

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
        myCompositeDisposable?.clear()
    }

    override fun showAddTravel() {
        val addTravelDialog = AddTravelDialog()
        addTravelDialog.onOk = {
            val sessionCredentials = SharedPreferencesUtils.getSessionCredentials(this)
            val travelName = addTravelDialog.travelName.text.toString()
            presenter.addTravel(sessionCredentials.userId, sessionCredentials.authToken, travelName)
        }
        addTravelDialog.show(supportFragmentManager, addTravelDialog.TAG)
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
        recyclerViewTravels.adapter?.notifyDataSetChanged()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayoutTravels, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    override fun showAddTravelResult(result: ADD_TRAVEL_ANSWER) {
        when (result) {
            ADD_TRAVEL_ANSWER.OK -> presenter.loadTravels()
            ADD_TRAVEL_ANSWER.ERROR -> showSnackbar(resources.getString(R.string.add_travel_error))
        }
    }

    override fun loadTravels(requestInterface: ServerApi, handleResponse: (myTravels: List<String>) -> Unit) {
        val sessionCredentials = SharedPreferencesUtils.getSessionCredentials(this)

        myCompositeDisposable?.add(requestInterface.getTravels(sessionCredentials.userId, sessionCredentials.authToken)
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
