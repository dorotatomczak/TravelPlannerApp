package com.github.travelplannerapp.scans

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.appmodel.Scan
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class ScansPresenter(view: ScansContract.View, private val travelId: Int) : BasePresenter<ScansContract.View>(view), ScansContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var scans = ArrayList<Scan>()
    private var scansToDelete = mutableSetOf<Scan>()

    override fun onAddScanClicked() {
        if (view.verifyPermissions()) {
            view.openCamera()
        } else {
            view.requestPermissions()
        }
    }

    override fun onPhotoTaken() {
        view.showScanner(travelId)
    }

    override fun loadScans() {
        view.setLoadingIndicatorVisibility(true)

        compositeDisposable.add(CommunicationService.serverApi.getScans(SharedPreferencesUtils.getUserId(), travelId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { scans -> handleLoadScansResponse(scans) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun getScansCount(): Int {
        return scans.size
    }

    override fun onBindScansAtPosition(position: Int, itemView: ScansContract.ScanItemView) {
        val scan = scans[position]
        itemView.setImage(CommunicationService.getScanUrl(scan.name, SharedPreferencesUtils.getUserId()))
        itemView.setCheckbox()
    }

    override fun onScanAdded(scan: Scan) {
        scans.add(scan)
        view.showScans()
        view.onDataSetChanged()
    }

    override fun onScanClicked(position: Int) {
        val scan = scans[position]
        view.showFullScan((CommunicationService.getScanUrl(scan.name, SharedPreferencesUtils.getUserId())))
    }

    override fun onDeleteClicked() {
        if (scansToDelete.size > 0) {
            view.showConfirmationDialog()
        }
    }

    override fun deleteScans() {
        compositeDisposable.add(CommunicationService.serverApi.deleteScans(SharedPreferencesUtils.getUserId(), scansToDelete)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { handleDeleteScansResponse() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun enterActionMode() {
        view.onDataSetChanged()
        view.showActionMode()
    }

    override fun leaveActionMode() {
        view.onDataSetChanged()
        view.showNoActionMode()
    }

    override fun addScanToDelete(position: Int) {
        scansToDelete.add(scans[position])
    }

    override fun removeScanToDelete(position: Int) {
        scansToDelete.remove(scans[position])
    }

    private fun handleLoadScansResponse(scans: List<Scan>) {
        this.scans = ArrayList(scans)
        view.onDataSetChanged()
        view.setLoadingIndicatorVisibility(false)

        if (this.scans.isEmpty()) view.showNoScans() else view.showScans()
    }

    private fun handleDeleteScansResponse() {
        scansToDelete.clear()
        loadScans()
        leaveActionMode()
        view.showSnackbar(R.string.delete_scans_ok)
    }

    private fun handleErrorResponse(error: Throwable) {
        scansToDelete.clear()
        loadScans()
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

}
