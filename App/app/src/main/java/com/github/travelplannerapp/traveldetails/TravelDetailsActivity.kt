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
import com.github.travelplannerapp.sharetraveldialog.ShareTravelDialog
import com.github.travelplannerapp.traveldetails.addplanelement.AddPlanElementActivity
import com.github.travelplannerapp.traveldialog.TravelDialog
import com.github.travelplannerapp.utils.DrawerUtils
import com.github.travelplannerapp.utils.copyInputStreamToFile
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_travel_details.*
import kotlinx.android.synthetic.main.fab_add.*
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_details)

        fabAdd.setOnClickListener { presenter.onAddPlanElementClicked() }

        swipeRefreshLayoutTravelDetails.setOnRefreshListener { presenter.loadDayPlans() }

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        val travel = intent.getSerializableExtra(EXTRA_TRAVEL) as Travel
        DrawerUtils.getDrawer(this, toolbar, travel.id)

        recyclerViewDayPlans.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewDayPlans.adapter = TravelDetailsAdapter(presenter)

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
                    refreshDayPlans()
                }
            }
            REQUEST_SHOW_DETAILS -> {
                if (resultCode == Activity.RESULT_OK) {
                    refreshDayPlans()
                }
            }
        }
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
        fabAdd.visibility = View.GONE
    }

    override fun showNoActionMode() {
        fabAdd.visibility = View.VISIBLE
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

    override fun showPlanElementDetails(placeId: Int, place: Place, placeTitle: String) {
        val intent = Intent(this, PlanElementDetailsActivity::class.java)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_PLACE_HREF, place.href)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_AVERAGE_RATING, place.averageRating)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_PLACE_NAME, placeTitle)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_PLACE_ID, placeId)

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

    override fun sharePlanElement(planElementName: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val body = getString(R.string.plan_element_completed) + planElementName
        intent.putExtra(Intent.EXTRA_TEXT, body)
        startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
    }

    private fun showEditTravel() {
        val editTravelDialog = TravelDialog(getString(R.string.change_travel_name))
        editTravelDialog.onOk = {
            val travelName = editTravelDialog.travelName.text.toString()
            presenter.changeTravelName(travelName)
        }
        editTravelDialog.show(supportFragmentManager, TravelDialog.TAG)
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

    private fun showImageSelection() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, REQUEST_SELECT_IMAGE)
    }

    private fun refreshDayPlans() {
        swipeRefreshLayoutTravelDetails.isRefreshing = true
        presenter.loadDayPlans()
    }
}
