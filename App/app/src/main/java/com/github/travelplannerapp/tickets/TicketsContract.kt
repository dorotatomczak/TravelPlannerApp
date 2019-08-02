package com.github.travelplannerapp.tickets

interface TicketsContract {
    interface View {
        fun verifyPermissions(): Boolean
        fun requestPermissions()
        fun openCamera()
    }

    interface Presenter {
        fun onAddTravelClick()
    }
}