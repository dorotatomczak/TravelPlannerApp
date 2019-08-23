package com.github.travelplannerapp.tickets

interface TicketsContract {
    interface View {
        fun verifyPermissions(): Boolean
        fun requestPermissions()
        fun openCamera()
        fun showScanner(travelId: Int)
    }

    interface Presenter {
        fun onAddTravelClick()
        fun onPhotoTaken()
    }
}