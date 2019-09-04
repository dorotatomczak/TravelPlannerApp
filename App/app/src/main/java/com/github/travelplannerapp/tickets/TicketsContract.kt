package com.github.travelplannerapp.tickets

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
        fun hideLoadingIndicator()
        fun showFullScan(url: String)
    }

    interface TicketItemView {
        fun setImage(url: String)
    }

    interface Presenter {
        fun onAddScanClick()
        fun onPhotoTaken()
        fun loadScans()
        fun unsubscribe()
        fun getTicketsCount(): Int
        fun onBindTravelsAtPosition(position:Int, itemView: TicketItemView)
        fun onAddedScan(name: String)
        fun onScanClicked(position: Int)
    }
}
