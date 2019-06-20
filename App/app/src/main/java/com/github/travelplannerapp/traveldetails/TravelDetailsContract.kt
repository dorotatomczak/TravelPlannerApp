package com.github.travelplannerapp.traveldetails

interface TravelDetailsContract {

    interface View {
        fun showTravelName(name: String)
    }

    interface Presenter {
        fun loadTravel()
    }
}