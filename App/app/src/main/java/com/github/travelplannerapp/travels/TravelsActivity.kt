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
import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity
import com.github.travelplannerapp.travels.dialogs.AddEditTravelDialog
import com.github.travelplannerapp.travels.dialogs.RecommendedPlacesDialog
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

    companion object {
        const val EXTRA_RECOMMENDED_PLACES = "EXTRA_RECOMMENDED_PLACES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.travels)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        fabAdd.setOnClickListener {
            showAddTravel()
        }

        swipeRefreshLayoutTravels.setOnRefreshListener { presenter.loadTravels() }

        recyclerViewTravels.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewTravels.adapter = TravelsAdapter(presenter)

        val recommendedPlaces: HashMap<String, String>? = intent.getSerializableExtra(EXTRA_RECOMMENDED_PLACES) as? HashMap<String, String>
        if (recommendedPlaces != null) {
            showRecommendedPlacesDialog(ArrayList(recommendedPlaces.values))
        }

        presenter.loadTravels()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun showAddTravel() {
        val addTravelDialog = AddEditTravelDialog(getString(R.string.new_travel))
        addTravelDialog.onOk = {
            val travelName = addTravelDialog.travelName.text.toString()
            presenter.addTravel(travelName)
        }
        addTravelDialog.show(supportFragmentManager, AddEditTravelDialog.TAG)
    }

    override fun showTravelDetails(travel: Travel) {
        val intent = Intent(this, TravelDetailsActivity::class.java)
        intent.putExtra(TravelDetailsActivity.EXTRA_TRAVEL, travel)
        startActivityForResult(intent, TravelDetailsActivity.REQUEST_TRAVEL_DETAILS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TravelDetailsActivity.REQUEST_TRAVEL_DETAILS -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val travel = data.getSerializableExtra(TravelDetailsActivity.EXTRA_TRAVEL) as Travel
                    presenter.updateTravel(travel)
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

    private fun showRecommendedPlacesDialog(places: List<String>) {
        val recommendedPlacesDialog = RecommendedPlacesDialog(places, getString(R.string.recommended_places))
        recommendedPlacesDialog.show(supportFragmentManager, RecommendedPlacesDialog.TAG)
    }
}
