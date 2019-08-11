package com.github.travelplannerapp.scanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_scanner.*
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import javax.inject.Inject

class ScannerActivity : AppCompatActivity(), ScannerContract.View {

    @Inject
    lateinit var presenter: ScannerContract.Presenter

    private var photoPath: String? = null

    companion object {
        const val REQUEST_SCANNER = 3
        const val REQUEST_SCANNER_RESULT = "REQUEST_SCANNER_RESULT"
        const val PHOTO_PATH_EXTRA = "PHOTO_PATH_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        initOpenCv()

        buttonScan.setOnClickListener { presenter.takeScan() }

        photoPath = intent.getStringExtra(PHOTO_PATH_EXTRA)
        if (photoPath != null) {
            imageViewSelection.setImageBitmap(BitmapHelper.decodeBitmapFromFile(photoPath!!,
                    resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels))
//            imageViewSelection.setPoints(Scanner.findCorners(photoPath))
        } else returnResultAndFinish(R.string.scanner_initialization_failure)
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

    private fun initOpenCv() {
        if (!OpenCVLoader.initDebug()) {
            returnResultAndFinish(R.string.scanner_initialization_failure)
        }
    }
}
