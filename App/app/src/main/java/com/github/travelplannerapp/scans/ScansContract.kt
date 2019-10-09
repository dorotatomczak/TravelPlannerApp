package com.github.travelplannerapp.scans

import com.github.travelplannerapp.communication.appmodel.Scan
import com.github.travelplannerapp.deleteactionmode.DeleteContract

interface ScansContract {
    interface View : DeleteContract.View {
        fun verifyPermissions(): Boolean
        fun requestPermissions()
        fun openCamera()
        fun showScanner(travelId: Int)
        fun showSnackbar(messageCode: Int)
        fun showScans()
        fun showNoScans()
        fun onDataSetChanged()
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
        fun showFullScan(url: String)
    }

    interface ScanItemView : DeleteContract.ItemView {
        fun setImage(url: String)
    }

    interface Presenter : DeleteContract.Presenter {
        fun onAddScanClicked()
        fun onPhotoTaken()
        fun loadScans()
        fun unsubscribe()
        fun getScansCount(): Int
        fun onBindScansAtPosition(position: Int, itemView: ScanItemView)
        fun onScanAdded(scan: Scan)
        fun onScanClicked(position: Int)
        fun deleteScans()
        fun addScanToDelete(position: Int)
        fun removeScanToDelete(position: Int)
    }
}
