package com.github.travelplannerapp.tickets

import com.github.travelplannerapp.deleteactionmode.DeleteContract
import com.github.travelplannerapp.communication.model.Scan

interface TicketsContract {
    interface View : DeleteContract.View {
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
    }

    interface TicketItemView : DeleteContract.ItemView {
        fun setImage(url: String)
    }

    interface Presenter : DeleteContract.Presenter {
        fun onAddScanClicked()
        fun onPhotoTaken()
        fun loadScans()
        fun unsubscribe()
        fun getTicketsCount(): Int
        fun onBindTicketsAtPosition(position: Int, itemView: TicketItemView)
        fun onScanAdded(scan: Scan)
        fun onScanClicked(position: Int)
        fun deleteTickets()
        fun addTicketToDelete(position: Int)
        fun removeTicketToDelete(position: Int)
    }
}
