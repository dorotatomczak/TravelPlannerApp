package com.github.travelplannerapp.scanner

import android.graphics.PointF
import org.opencv.core.Mat

interface ScannerContract {

    interface View {
        fun closeScanner()
        fun showPreview(scan: Mat)
        fun returnResultAndFinish(messageCode: Int)
    }

    interface Presenter {
        fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int)
    }

}