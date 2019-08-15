package com.github.travelplannerapp.scanner

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ServerApi
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scanner.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.opencv.android.OpenCVLoader
import javax.inject.Inject

class ScannerActivity : AppCompatActivity(), ScannerContract.View {

    @Inject
    lateinit var presenter: ScannerContract.Presenter

    private var photoPath: String? = null
    var myCompositeDisposable = CompositeDisposable()

    companion object {
        const val REQUEST_SCANNER = 3
        const val REQUEST_SCANNER_RESULT = "REQUEST_SCANNER_RESULT"
        const val PHOTO_PATH_EXTRA = "PHOTO_PATH_EXTRA"
    }

    data class TokenAndEmail(val token: String, val email: String)

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

        photoPath = intent.getStringExtra(PHOTO_PATH_EXTRA)
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
            presenter.uploadScan(scanFile)
        }

        dialog.setNegativeButton(R.string.cancel) { _, _ ->
            finish()
        }

        dialog.show()
    }

    override fun uploadScan(requestInterface: ServerApi, part: MultipartBody.Part,
                            handleResponse: (jsonString: String) -> Unit) {

        val (token, email) = getTokenAndEmail()
        val emailReqBody = RequestBody.create(MediaType.parse("text/plain"), email)

        myCompositeDisposable.add(requestInterface.uploadScan(token, emailReqBody, part)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(handleResponse))
    }

    //TODO [Dorota] Move to common file (sharedPreferencesUtils or similar)
    private fun getTokenAndEmail(): TokenAndEmail {
        val sharedPref = getSharedPreferences(resources.getString(R.string.auth_settings),
                Context.MODE_PRIVATE)
        val email = sharedPref.getString(resources.getString(R.string.email_shared_pref),
                "default").toString()
        val authToken = sharedPref.getString(resources.getString(R.string.auth_token_shared_pref),
                "default").toString()

        return TokenAndEmail(authToken, email)
    }

}
