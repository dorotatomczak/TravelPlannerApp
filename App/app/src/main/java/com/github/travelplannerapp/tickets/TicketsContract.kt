package com.github.travelplannerapp.tickets

import com.github.travelplannerapp.communication.model.Scan

interface TicketsContract {
    interface View {
        fun verifyPermissions(): Boolean
        fun requestPermissions()
        fun openCamera()
        fun showScanner(travelId: Int)
        fun showSnackbar(messageCode: Int)
        fun showTickets()
        fun showNoTickets()
        fun onDataSetChanged()
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
        fun showFullScan(url: String)
        fun showActionMode()
        fun showNoActionMode()
        fun showConfirmationDialog()
    }

    interface TicketItemView {
        fun setImage(url: String)
        fun setCheckbox()
    }

    interface Presenter {
        fun onAddScanClick()
        fun onPhotoTaken()
        fun loadScans()
        fun unsubscribe()
        fun getTicketsCount(): Int
        fun onBindTicketsAtPosition(position:Int, itemView: TicketItemView)
        fun onAddedScan(scan: Scan)
        fun onScanClicked(position: Int)
        fun deleteTickets()
        fun onDeleteClicked()
        fun enterActionMode()
        fun leaveActionMode()
        fun addTicketToDelete(position: Int)
        fun removeTicketToDelete(position: Int)
    }
}
