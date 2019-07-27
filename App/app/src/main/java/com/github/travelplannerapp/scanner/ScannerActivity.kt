package com.github.travelplannerapp.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import org.opencv.android.OpenCVLoader
import javax.inject.Inject

class ScannerActivity : AppCompatActivity(), ScannerContract.View {
    @Inject
    lateinit var presenter: ScannerContract.Presenter

    companion object {
        const val REQUEST_PERMISSIONS = 0
        const val REQUEST_SCANNER = 1
        const val REQUEST_SCANNER_RESULT = "REQUEST_SCANNER_RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        verifyPermissions()
        initOpenCV()
    }

    private fun initOpenCV(){
        if (!OpenCVLoader.initDebug()) {
            val resultIntent = Intent()
            resultIntent.putExtra(REQUEST_SCANNER_RESULT, resources.getString(R.string.scanner_initialization_failure))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun verifyPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)

        if(ContextCompat.checkSelfPermission(this.applicationContext,
                        permissions[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.applicationContext,
                        permissions[1]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.applicationContext,
                        permissions[2]) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
        }else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    val resultIntent = Intent()
                    resultIntent.putExtra(REQUEST_SCANNER_RESULT, resources.getString(R.string.scanner_permissions_failure))
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }
}
