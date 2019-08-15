package com.github.travelplannerapp.travels

import com.github.travelplannerapp.communication.ServerApi

interface TravelsContract {
    interface View {
        fun showAddTravel()
        //TODO [Dorota] Change to int (travel id) after database is implemented
        fun showTravelDetails(travel: String)

        fun showTravels()

        fun showNoTravels()

        fun showSnackbar(message: String)

        fun loadTravels(requestInterface: ServerApi, handleResponse: (myTravels: List<String>) -> Unit)

        fun addTravel(requestInterface: ServerApi, jsonAddTravelRequest: String, handleResponse: (jsonString: String) -> Unit)
    }

    interface TravelItemView {
        fun setName(name: String)
    }

    interface Presenter {

        fun loadTravels()

        fun getTravelsCount(): Int

        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)

        fun openTravelDetails(position: Int)

        fun handleResponse(myTravels: List<String>)
    }
}
