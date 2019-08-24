package com.github.travelplannerapp.tickets

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.BuildConfig
import com.github.travelplannerapp.R
import com.github.travelplannerapp.scanner.ScannerActivity
import com.github.travelplannerapp.utils.DrawerUtils
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_tickets.*
import kotlinx.android.synthetic.main.fab_add.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject

class TicketsActivity : AppCompatActivity(), TicketsContract.View {

    companion object {
        const val REQUEST_PERMISSIONS = 0
        const val REQUEST_TAKE_PHOTO = 1
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
    }

    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    @Inject
    lateinit var presenter: TicketsContract.Presenter
    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        fabAdd.setOnClickListener {
            presenter.onAddScanClick()
        }

        recyclerViewTickets.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewTickets.adapter = TicketsAdapter(presenter)

        presenter.loadScans(
                SharedPreferencesUtils.getAccessToken(this)!!,
                SharedPreferencesUtils.getUserId(this)
        )
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    photoPath?.let { presenter.onPhotoTaken() }
                }
            }
            ScannerActivity.REQUEST_SCANNER -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val messageCode = data.getIntExtra(ScannerActivity.REQUEST_SCANNER_RESULT_MESSAGE,
                            R.string.scanner_general_error)
                    showSnackbar(messageCode)
                    val scanName = data.getStringExtra(ScannerActivity.REQUEST_SCANNER_RESULT_NAME)
                    presenter.onAddedScan(scanName)
                }
            }
        }
    }

    override fun verifyPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        requiredPermissions[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.applicationContext,
                        requiredPermissions[1]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.applicationContext,
                        requiredPermissions[2]) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    override fun requestPermissions() {
        ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    showSnackbar(R.string.scanner_permissions_error)
                } else {
                    openCamera()
                }
            }
        }
    }

    override fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhotoIntent ->
            takePhotoIntent.resolveActivity(packageManager)?.also {
                val photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    showSnackbar(R.string.scanner_initialization_error)
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".fileprovider",
                            it
                    )
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun showScanner(travelId: Int) {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra(ScannerActivity.EXTRA_PHOTO_PATH, photoPath)
        intent.putExtra(ScannerActivity.EXTRA_TRAVEL_ID, travelId)
        startActivityForResult(intent, ScannerActivity.REQUEST_SCANNER)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutTickets, getString(messageCode), Snackbar.LENGTH_SHORT).show()
    }

    override fun showTickets() {
        textViewNoTickets.visibility = View.GONE
        recyclerViewTickets.visibility = View.VISIBLE
    }

    override fun showNoTickets() {
        textViewNoTickets.visibility = View.VISIBLE
        recyclerViewTickets.visibility = View.GONE
    }

    override fun onDataSetChanged() {
        recyclerViewTickets.adapter?.notifyDataSetChanged()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            photoPath = absolutePath
        }
    }
}
