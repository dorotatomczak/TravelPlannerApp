package com.github.travelplannerapp.traveldetails

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.travelplannerapp.accommodation.AccommodationActivity
import com.github.travelplannerapp.traveldialog.TravelDialog
import com.github.travelplannerapp.dayplans.DayPlansActivity
import com.github.travelplannerapp.scans.ScansActivity
import com.github.travelplannerapp.transport.TransportActivity
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import javax.inject.Inject

import kotlinx.android.synthetic.main.activity_travel_details.*
import kotlinx.android.synthetic.main.toolbar.*
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.utils.copyInputStreamToFile
import java.io.File


class TravelDetailsActivity : AppCompatActivity(), TravelDetailsContract.View {

    @Inject
    lateinit var presenter: TravelDetailsContract.Presenter

    companion object {
        const val EXTRA_TRAVEL = "EXTRA_TRAVEL"
        const val REQUEST_TRAVEL_DETAILS = 1
        const val REQUEST_SELECT_IMAGE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_details)

        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        recyclerViewTravelDetails.setHasFixedSize(true)
        recyclerViewTravelDetails.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewTravelDetails.adapter = TravelDetailsAdapter(presenter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_travel_details, menu)
        menu.findItem(R.id.menuEdit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSetPhoto) {
            showImageSelection()
            return true
        } else if (item.itemId == R.id.menuEdit) {
            showEditTravel()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTravel()
    }

    override fun setTitle(title: String) {
        collapsing.title = title
    }

    override fun setResult(travel: Travel) {
        val resultIntent = Intent().apply {
            putExtra(EXTRA_TRAVEL, travel)
        }
        setResult(RESULT_OK, resultIntent)
    }

    override fun showDayPlans(travelId: Int) {
        val intent = Intent(this, DayPlansActivity::class.java)
        intent.putExtra(DayPlansActivity.EXTRA_TRAVEL_ID, travelId)
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

    override fun showScans(travelId: Int) {
        val intent = Intent(this, ScansActivity::class.java)
        intent.putExtra(ScansActivity.EXTRA_TRAVEL_ID, travelId)
        startActivity(intent)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutTravelDetails, getString(messageCode), Snackbar.LENGTH_LONG).show()
    }

    override fun showImage(url: String) {
        Glide.with(this)
                .apply { RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) }
                .load(url)
                .into(imageTravelDetails)
    }

    private fun showEditTravel() {
        val editTravelDialog = TravelDialog(getString(R.string.change_travel_name))
        editTravelDialog.onOk = {
            val travelName = editTravelDialog.travelName.text.toString()
            presenter.changeTravelName(travelName)
        }
        editTravelDialog.show(supportFragmentManager, TravelDialog.TAG)
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
}
