package com.github.travelplannerapp.scanner

import android.graphics.PointF
import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonScanUploadResponse
import com.github.travelplannerapp.jsondatamodels.ScanUploadResponse
import com.google.gson.Gson
import okhttp3.MediaType
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody

class ScannerPresenter(view: ScannerContract.View) : BasePresenter<ScannerContract.View>(view), ScannerContract.Presenter {

    override fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int) {
        val scan = Scanner.cropAndScan(photoPath, corners, scaleRatio)
        if (scan != null) view.showScanResultDialog(scan)
        else view.returnResultAndFinish(R.string.scanner_general_failure)
    }

    override fun uploadScan(scan: File?) {
        if (scan == null) view.returnResultAndFinish(R.string.scan_upload_failure)
        val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), scan!!)
        val filePart = MultipartBody.Part.createFormData("file", scan.name, fileReqBody)
        view.uploadScan(CommunicationService.serverApi, filePart, this::handleUploadResponse)
    }

    override fun handleUploadResponse(jsonString: String) {
        val response = Gson().fromJson(jsonString, JsonScanUploadResponse::class.java)
        response?.let {
            when (it.result) {
                ScanUploadResponse.OK -> view.returnResultAndFinish(R.string.scanner_success)
                ScanUploadResponse.ERROR-> view.returnResultAndFinish(R.string.scan_upload_failure)
            }
        }
    }

}