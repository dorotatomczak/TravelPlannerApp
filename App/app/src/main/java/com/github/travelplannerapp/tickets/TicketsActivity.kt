package com.github.travelplannerapp.tickets

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.travelplannerapp.BuildConfig
import com.github.travelplannerapp.R
import com.github.travelplannerapp.scanner.ScannerActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_tickets.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject

class TicketsActivity : AppCompatActivity(), TicketsContract.View, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val REQUEST_PERMISSIONS = 0
        const val REQUEST_TAKE_PHOTO = 1
    }

    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    @Inject
    lateinit var presenter: TicketsContract.Presenter

    private lateinit var toggle: ActionBarDrawerToggle

    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)
        setSupportActionBar(toolbarTickets)
        supportActionBar?.setHomeButtonEnabled(true)

        toggle = ActionBarDrawerToggle(this, drawerLayoutTickets, toolbarTickets, R.string.drawer_open, R.string.drawer_close)
        drawerLayoutTickets.addDrawerListener(toggle)
        toggle.syncState()

        navigationViewTickets.setNavigationItemSelectedListener(this)

        fabTickets.setOnClickListener {
            presenter.onAddTravelClick()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //TODO [Dorota] Same as in travels, move to common file
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    photoPath?.let {showScanner()}
                }
            }
            ScannerActivity.REQUEST_SCANNER -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val messageCode = data.getIntExtra(ScannerActivity.REQUEST_SCANNER_RESULT,
                            R.string.scanner_general_failure)
                    showSnackbar(messageCode)
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
                    showSnackbar(R.string.scanner_permissions_failure)
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
                    showSnackbar(R.string.scanner_initialization_failure)
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

    private fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutTickets, getString(messageCode), Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
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

    private fun showScanner() {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra(ScannerActivity.PHOTO_PATH_EXTRA, photoPath)
        startActivityForResult(intent, ScannerActivity.REQUEST_SCANNER)
    }
}
