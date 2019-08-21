package com.github.travelplannerapp.travels


interface TravelsContract {
    interface View {
        fun showAddTravel()
        fun showTravelDetails(travelId: Int)
        fun showTravels()
        fun showNoTravels()
        fun showSnackbar(messageCode: Int)
        fun showSnackbar(message: String)
        fun onDataSetChanged()
    }

    interface TravelItemView {
        fun setName(name: String)
    }

    interface Presenter {
        fun loadTravels(token: String, userId: Int)
        fun addTravel(userId: Int, token: String, travelName: String)
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun unsubscribe()
    }
}
