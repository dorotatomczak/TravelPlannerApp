package com.github.travelplannerapp.scanner

import android.graphics.PointF
import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody

class ScannerPresenter(view: ScannerContract.View, private val travelId: Int) : BasePresenter<ScannerContract.View>(view), ScannerContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int) {
        val scan = Scanner.cropAndScan(photoPath, corners, scaleRatio)
        if (scan != null) view.showScanResultDialog(scan)
        else view.returnResultAndFinish(R.string.scanner_general_failure)
    }

    override fun uploadScan(scan: File?, token: String, userId: Int) {
        if (scan == null) view.returnResultAndFinish(R.string.scan_upload_failure)
        val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), scan!!)
        val filePart = MultipartBody.Part.createFormData("file", scan.name, fileReqBody)
        val userIdReqBody =  RequestBody.create(MediaType.parse("text/plain"), userId.toString())
        val travelIdReqBody = RequestBody.create(MediaType.parse("text/plain"), travelId.toString())

        compositeDisposable.add(CommunicationService.serverApi.uploadScan(token, userIdReqBody, travelIdReqBody, filePart)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.statusCode == 200) it.data else throw ApiException(it.statusCode) }
                .subscribe(
                        { scanName -> handleUploadResponse(scanName) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleUploadResponse(scanName: String?) {
        scanName?.let {view.returnResultAndFinish(R.string.scanner_success, scanName)}
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.returnResultAndFinish(error.getErrorMessageCode())
        else view.returnResultAndFinish(R.string.server_connection_error)
    }

}