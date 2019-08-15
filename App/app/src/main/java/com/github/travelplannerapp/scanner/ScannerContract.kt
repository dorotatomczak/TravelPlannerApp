package com.github.travelplannerapp.scanner

import android.graphics.Bitmap
import android.graphics.PointF
import com.github.travelplannerapp.communication.ServerApi
import okhttp3.MultipartBody
import java.io.File

interface ScannerContract {

    interface View {
        fun showScanResultDialog(scan: Bitmap)
        fun returnResultAndFinish(messageCode: Int)
        fun uploadScan(requestInterface: ServerApi, part: MultipartBody.Part,
                       handleResponse: (jsonString: String) -> Unit)
    }

    interface Presenter {
        fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int)
        fun uploadScan(scan: File?)
        fun handleUploadResponse(jsonString: String)
    }

}