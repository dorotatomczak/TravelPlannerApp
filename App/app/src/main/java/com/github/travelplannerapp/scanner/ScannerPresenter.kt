package com.github.travelplannerapp.scanner

import android.graphics.PointF
import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.appmodel.Scan
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ScannerPresenter(view: ScannerContract.View, private val travelId: Int) : BasePresenter<ScannerContract.View>(view), ScannerContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun takeScan(photoPath: String, corners: List<PointF>, scaleRatio: Int) {
        val scan = Scanner.cropAndScan(photoPath, corners, scaleRatio)
        if (scan != null) view.showScanResultDialog(scan)
        else view.returnResultAndFinish(R.string.scanner_general_error)
    }

    override fun uploadScan(scan: File?, token: String, userId: Int) {
        if (scan == null) view.returnResultAndFinish(R.string.scan_upload_error)
        val fileReqBody = scan!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", scan.name, fileReqBody)
        val travelIdReqBody = travelId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        compositeDisposable.add(CommunicationService.serverApi.uploadScan(
                SharedPreferencesUtils.getUserId(),
                travelIdReqBody,
                filePart)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                .subscribe(
                        { addedScan -> handleUploadResponse(addedScan) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleUploadResponse(scan: Scan?) {
        scan?.let { view.returnResultAndFinish(R.string.scanner_success, scan) }
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.returnResultAndFinish(error.getErrorMessageCode())
        else view.returnResultAndFinish(R.string.server_connection_error)
    }

}
