package com.github.travelplannerapp.traveldetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.UserInfo
import com.github.travelplannerapp.planelementdetails.PlanElementDetailsActivity
import com.github.travelplannerapp.traveldetails.addplanelement.AddPlanElementActivity
import com.github.travelplannerapp.traveldetails.addtransport.AddTransportActivity
import com.github.travelplannerapp.travels.dialogs.AddEditTravelDialog
import com.github.travelplannerapp.travels.dialogs.ShareTravelDialog
import com.github.travelplannerapp.utils.DrawerUtils
import com.github.travelplannerapp.utils.copyInputStreamToFile
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_travel_details.*
import kotlinx.android.synthetic.main.element_add_transport.*
import kotlinx.android.synthetic.main.fab_add_extended.*
import kotlinx.android.synthetic.main.fab_cancel.*
import kotlinx.android.synthetic.main.fab_next.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import javax.inject.Inject


class TravelDetailsActivity : AppCompatActivity(), TravelDetailsContract.View {

    @Inject
    lateinit var presenter: TravelDetailsContract.Presenter

    companion object {
        const val EXTRA_TRAVEL = "EXTRA_TRAVEL"
        const val REQUEST_TRAVEL_DETAILS = 1
        const val REQUEST_SELECT_IMAGE = 2
        const val REQUEST_ADD_PLAN = 3
        const val REQUEST_SHOW_DETAILS = 4
        const val REQUEST_SHOW_ADD_TRANSPORT = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_details)

        fabAddExtended.setOnClickListener { toggleFabMenu() }
        fabPlanElement.setOnClickListener { presenter.onAddPlanElementClicked() }
        fabTransport.setOnClickListener { showSelectTransportPoints() }

        fabCancel.setOnClickListener { hideSelectTransportPoints() }
        fabNext.setOnClickListener { presenter.onNextClicked() }

        swipeRefreshLayoutTravelDetails.setOnRefreshListener { presenter.loadDayPlans() }

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        val travel = intent.getSerializableExtra(EXTRA_TRAVEL) as Travel
        DrawerUtils.getDrawer(this, toolbar, travel.id)

        recyclerViewDayPlans.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewDayPlans.adapter = TravelDetailsAdapter(presenter)

        hideSelectTransportPoints()
        presenter.loadTravel()
        presenter.loadFriendsWithoutAccessToTravel()
        refreshDayPlans()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_share_travel, menu)
        menu.findItem(R.id.menuShareTravelItem).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuInflater.inflate(R.menu.menu_travel_details, menu)
        menu.findItem(R.id.menuEdit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSetPhoto) {
            showImageSelection()
        } else if (item.itemId == R.id.menuEdit) {
            showEditTravel()
        } else if (item.itemId == R.id.menuShareTravelItem) {
            presenter.onShareTravelClicked()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SELECT_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.data

                    selectedImage?.let {
                        contentResolver.openInputStream(selectedImage)?.use { inputStream ->
                            val file = File.createTempFile("TRAVEL_", ".jpg", cacheDir)
                            file.copyInputStreamToFile(inputStream)
                            presenter.uploadTravelImage(file)
                        }
                    }
                }
            }
            REQUEST_ADD_PLAN -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val messageCode = data.getIntExtra(AddPlanElementActivity.REQUEST_ADD_PLAN_ELEMENT_RESULT_MESSAGE,
                            R.string.try_again)
                    showSnackbar(messageCode)
                    val plan = data.getSerializableExtra(AddPlanElementActivity.REQUEST_ADD_PLAN_ELEMENT_RESULT_PLAN) as PlanElement
                    presenter.onPlanElementAdded(plan)
                }
            }
        }
        hideSelectTransportPoints()
        collapseFabMenu()
        refreshDayPlans()
    }

    override fun setTitle(title: String) {
        collapsing.title = title
    }

    override fun setResult(travel: Travel) {
        val resultIntent = Intent().putExtra(EXTRA_TRAVEL, travel)
        setResult(RESULT_OK, resultIntent)
    }

    override fun showImage(url: String) {
        Glide.with(this)
                .apply { RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) }
                .load(url)
                .into(imageTravelDetails)
    }

    override fun showDayPlans() {
        textViewNoDayPlans.visibility = View.GONE
        recyclerViewDayPlans.visibility = View.VISIBLE
    }

    override fun showNoDayPlans() {
        textViewNoDayPlans.visibility = View.VISIBLE
        recyclerViewDayPlans.visibility = View.GONE
    }

    override fun showAddPlanElement(travelId: Int) {
        val intent = Intent(this, AddPlanElementActivity::class.java)
        intent.putExtra(AddPlanElementActivity.EXTRA_TRAVEL_ID, travelId)
        startActivityForResult(intent, REQUEST_ADD_PLAN)
    }

    override fun onDataSetChanged() {
        recyclerViewDayPlans.adapter?.notifyDataSetChanged()
    }

    override fun hideLoadingIndicator() {
        swipeRefreshLayoutTravelDetails.isRefreshing = false
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutTravelDetails, getString(messageCode), Snackbar.LENGTH_SHORT).show()
    }

    override fun showActionMode() {
        fabAddExtended.visibility = View.GONE
    }

    override fun showNoActionMode() {
        fabAddExtended.visibility = View.VISIBLE
        (recyclerViewDayPlans.adapter as TravelDetailsAdapter).leaveActionMode()
    }

    override fun showConfirmationDialog() {
        val travelsText = getString(R.string.day_plans)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_entry, travelsText))
                .setMessage(getString(R.string.delete_confirmation, travelsText))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    presenter.deletePlanElements()
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                }
                .show()
    }

    override fun showPlanElementDetails(planElement: PlanElement, placeTitle: String, travelId: Int) {
        val intent = Intent(this, PlanElementDetailsActivity::class.java)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_PLAN_ELEMENT, planElement)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_TRAVEL_ID, travelId)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_PLACE_NAME, placeTitle)

        val checkInString = getString(R.string.check_in)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_CAN_BE_RATED, !placeTitle.startsWith(checkInString))

        startActivityForResult(intent, REQUEST_SHOW_DETAILS)
    }

    override fun getAccommodationName(isCheckIn: Boolean, placeTitle: String): String {
        val resourceId: Int
        if (isCheckIn) resourceId = R.string.check_in
        else resourceId = R.string.check_out

        return getString(resourceId) + ": " + placeTitle
    }

    override fun sharePlanElement(urlToShare: String) {
        val regularFacebookApp = "com.facebook.katana"
        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        var isFacebookAppFound = false
        var appMatches = packageManager.queryIntentActivities(intent, 0)

        appMatches.forEach {
            if (it.activityInfo.packageName.toLowerCase().startsWith(regularFacebookApp)) {
                isFacebookAppFound = true
                intent.setPackage(it.activityInfo.packageName)
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare)
                startActivity(intent)
            }
        }
        if (!isFacebookAppFound) {
            showSnackbar(R.string.missing_facebook_app)
        }
    }

    override fun showShareTravel(friendsWithoutAccessToTravel: List<UserInfo>) {
        val shareTravelDialog = ShareTravelDialog(getString(R.string.share_travel), friendsWithoutAccessToTravel)
        shareTravelDialog.onOk = {
            val selectedFriendsIds = shareTravelDialog.selectedFriendsId
            if (selectedFriendsIds.size > 0) {
                presenter.shareTravel(selectedFriendsIds)
            } else {
                showSnackbar(R.string.no_selected_friends)
            }
        }
        shareTravelDialog.show(supportFragmentManager, ShareTravelDialog.TAG)
    }

    override fun showAddTransport(travelId: Int, fromPlace: Place, toPlace: Place, departureDate: Long) {
        val intent = Intent(this, AddTransportActivity::class.java)
        intent.putExtra(AddTransportActivity.EXTRA_FROM, fromPlace)
        intent.putExtra(AddTransportActivity.EXTRA_TO, toPlace)
        intent.putExtra(AddTransportActivity.EXTRA_DEPARTURE_DATE, departureDate)
        intent.putExtra(AddTransportActivity.EXTRA_TRAVEL_ID, travelId)

        startActivityForResult(intent, REQUEST_SHOW_ADD_TRANSPORT)
    }

    override fun fillTransportPoints(position: Int, placeTitle: String) {
        if (editTextFrom.isFocused) {
            editTextFrom.setText(placeTitle)
            presenter.onTransportFromPointFilled(position)
        } else if (editTextTo.isFocused) {
            editTextTo.setText(placeTitle)
            presenter.onTransportToPointFilled(position)
        }
    }

    private fun toggleFabMenu() {
        if (fabTransport.visibility == View.GONE) {
            expandFabMenu()
        } else {
            collapseFabMenu()
        }
    }

    private fun expandFabMenu() {
        fabTransport.visibility = View.VISIBLE
        fabPlanElement.visibility = View.VISIBLE
    }

    private fun collapseFabMenu() {
        fabTransport.visibility = View.GONE
        fabPlanElement.visibility = View.GONE
    }

    private fun showEditTravel() {
        val editTravelDialog = AddEditTravelDialog(getString(R.string.change_travel_name))
        editTravelDialog.onOk = {
            val travelName = editTravelDialog.travelName.text.toString()
            presenter.changeTravelName(travelName)
        }
        editTravelDialog.show(supportFragmentManager, AddEditTravelDialog.TAG)
    }

    private fun showImageSelection() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, REQUEST_SELECT_IMAGE)
    }

    private fun showSelectTransportPoints() {
        collapseFabMenu()
        fabAddExtended.visibility = View.GONE
        elementAddTransport.visibility = View.VISIBLE
        scrollViewExtender.visibility = View.VISIBLE

        presenter.enterTransportMode()
    }

    private fun hideSelectTransportPoints() {
        fabAddExtended.visibility = View.VISIBLE
        elementAddTransport.visibility = View.GONE
        scrollViewExtender.visibility = View.GONE

        presenter.leaveTransportMode()
    }

    private fun refreshDayPlans() {
        swipeRefreshLayoutTravelDetails.isRefreshing = true
        presenter.loadDayPlans()
    }
}
