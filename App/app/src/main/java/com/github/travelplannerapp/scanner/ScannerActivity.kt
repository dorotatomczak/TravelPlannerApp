package com.github.travelplannerapp.scanner

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_scanner.*
import org.opencv.android.OpenCVLoader
import javax.inject.Inject

class ScannerActivity : AppCompatActivity(), ScannerContract.View {

    @Inject
    lateinit var presenter: ScannerContract.Presenter

    private var photoPath: String? = null

    companion object {
        const val REQUEST_SCANNER = 3
        const val REQUEST_SCANNER_RESULT = "REQUEST_SCANNER_RESULT"
        const val EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH"
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        initOpenCv()

        buttonScan.setOnClickListener {
            photoPath?.let {
                presenter.takeScan(it, imageViewSelection.getPoints(),
                        imageViewSelection.scaleRatio)
            }
        }

        photoPath = intent.getStringExtra(EXTRA_PHOTO_PATH)
        if (photoPath != null) {
            val (bitmap, scaleRatio) = BitmapHelper.decodeBitmapFromFile(photoPath!!,
                    resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
            imageViewSelection.setImageBitmap(bitmap, scaleRatio)
//            imageViewSelection.setPoints(Scanner.findCorners(photoPath))
        } else returnResultAndFinish(R.string.scanner_initialization_failure)
    }

    override fun returnResultAndFinish(messageCode: Int) {
        val resultIntent = Intent().apply {
            putExtra(REQUEST_SCANNER_RESULT, messageCode)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun initOpenCv() {
        if (!OpenCVLoader.initDebug()) {
            returnResultAndFinish(R.string.scanner_initialization_failure)
        }
    }

    override fun showScanResultDialog(scan: Bitmap) {
        val dialog = AlertDialog.Builder(this)
        val imageView = ImageView(this)
        imageView.setImageBitmap(scan)
        dialog.setView(imageView)

        dialog.setPositiveButton(R.string.save) { _, _ ->
            val scanFile = BitmapHelper.bitmapToFile(scan, cacheDir)
            presenter.uploadScan(
                    scanFile,
                    SharedPreferencesUtils.getAccessToken(this)!!,
                    SharedPreferencesUtils.getUserId(this)
            )
        }

        dialog.setNegativeButton(R.string.cancel) { _, _ ->
            finish()
        }

        dialog.show()
    }

}
