package com.github.travelplannerapp.scanner

import android.graphics.Bitmap
import android.graphics.PointF
import java.io.File

interface ScannerContract {

    interface View {
        fun showScanResultDialog(scan: Bitmap)
        fun returnResultAndFinish(messageCode: Int)
    }

    interface Presenter {
        fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int)
        fun uploadScan(scan: File?, token: String, userId: Int)
    }

}