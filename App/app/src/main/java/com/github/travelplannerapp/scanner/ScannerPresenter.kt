package com.github.travelplannerapp.scanner

import android.graphics.PointF
import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R

class ScannerPresenter(view: ScannerContract.View) : BasePresenter<ScannerContract.View>(view), ScannerContract.Presenter {

    override fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int) {
        val scan = Scanner.cropAndScan(photoPath, corners, scaleRatio)
        if (scan != null) view.showScanResultDialog(scan)
        else view.returnResultAndFinish(R.string.scanner_general_failure)
    }

}