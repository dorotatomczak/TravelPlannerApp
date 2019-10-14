package com.github.travelplannerapp.travels

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity
import com.github.travelplannerapp.traveldialog.TravelDialog
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_travels.*
import kotlinx.android.synthetic.main.fab_add.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

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

        swipeRefreshLayoutTravels.setOnRefreshListener { presenter.loadTravels() }

        recyclerViewTravels.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewTravels.adapter = TravelsAdapter(presenter)

        presenter.loadTravels()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun showAddTravel() {
        val addTravelDialog = TravelDialog(getString(R.string.new_travel))
        addTravelDialog.onOk = {
            val travelName = addTravelDialog.travelName.text.toString()
            presenter.addTravel(travelName)
        }
        addTravelDialog.show(supportFragmentManager, TravelDialog.TAG)
    }

    override fun showTravelDetails(travelId: Int, travelName: String) {
        val intent = Intent(this, TravelDetailsActivity::class.java)
        intent.putExtra(TravelDetailsActivity.EXTRA_TRAVEL_ID, travelId)
        intent.putExtra(TravelDetailsActivity.EXTRA_TRAVEL_NAME, travelName)
        startActivityForResult(intent, TravelDetailsActivity.REQUEST_TRAVEL_DETAILS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TravelDetailsActivity.REQUEST_TRAVEL_DETAILS -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val travelId = data.extras!!.getInt(TravelDetailsActivity.EXTRA_TRAVEL_ID, 0)
                    val travelName = data.extras!!.getString(TravelDetailsActivity.EXTRA_TRAVEL_NAME)!!
                    presenter.updateTravelName(travelId, travelName)
                    onDataSetChanged()
                }
            }
        }
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

    override fun setLoadingIndicatorVisibility(isVisible: Boolean) {
        swipeRefreshLayoutTravels.isRefreshing = isVisible
    }

    override fun showActionMode() {
        fabAdd.visibility = View.GONE
    }

    override fun showNoActionMode() {
        fabAdd.visibility = View.VISIBLE
        (recyclerViewTravels.adapter as TravelsAdapter).leaveActionMode()
    }

    override fun showConfirmationDialog() {
        val travelsText = getString(R.string.travels)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_entry, travelsText))
                .setMessage(getString(R.string.delete_confirmation, travelsText))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    presenter.deleteTravels()
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                }
                .show()
    }
}
