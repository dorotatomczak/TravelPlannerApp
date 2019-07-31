package com.github.travelplannerapp.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_scanner.*
import org.opencv.android.OpenCVLoader
import javax.inject.Inject

class ScannerActivity : AppCompatActivity(), ScannerContract.View {

    @Inject
    lateinit var presenter: ScannerContract.Presenter

    companion object {
        const val REQUEST_SCANNER = 3
        const val REQUEST_SCANNER_RESULT = "REQUEST_SCANNER_RESULT"
        const val PHOTO_URI_EXTRA = "PHOTO_URI_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        buttonScan.setOnClickListener { presenter.takeScan() }

        val photoUri: Uri? = intent.getParcelableExtra(PHOTO_URI_EXTRA)
        if (photoUri != null) imageViewSelection.setImageURI(photoUri) else returnResultAndFinish(R.string.scanner_initialization_failure)
    }

    private fun returnResultAndFinish(messageCode: Int) {
        val resultIntent = Intent().apply {
            putExtra(REQUEST_SCANNER_RESULT, messageCode)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun closeScanner() {
        returnResultAndFinish(R.string.scanner_success)
    }

    private fun initOpenCV(){
        if (!OpenCVLoader.initDebug()) {
            val resultIntent = Intent()
            resultIntent.putExtra(REQUEST_SCANNER_RESULT, resources.getString(R.string.scanner_initialization_failure))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
