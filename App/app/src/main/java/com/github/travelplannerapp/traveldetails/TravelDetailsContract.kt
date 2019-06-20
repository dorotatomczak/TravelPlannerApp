package com.github.travelplannerapp.traveldetails

interface TravelDetailsContract {

    interface View {
        fun setTitle(title: String)
    }

    interface Presenter {
        fun loadTravel()
    }
}