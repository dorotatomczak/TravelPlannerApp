package com.github.travelplannerapp.scanner

import android.graphics.Bitmap
import android.graphics.PointF

interface ScannerContract {

    interface View {
        fun showScanResultDialog(scan: Bitmap)
        fun returnResultAndFinish(messageCode: Int)
    }

    interface Presenter {
        fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int)
    }

}