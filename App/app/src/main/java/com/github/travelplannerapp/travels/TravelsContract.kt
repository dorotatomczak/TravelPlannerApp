package com.github.travelplannerapp.travels


interface TravelsContract {
    interface View {
        fun showAddTravel()
        fun showTravelDetails(travelId: Int, travelName: String)
        fun showTravels()
        fun showNoTravels()
        fun showSnackbar(messageCode: Int)
        fun showSnackbar(message: String)
        fun onDataSetChanged()
        fun hideLoadingIndicator()
    }

    interface TravelItemView {
        fun setName(name: String)
    }

    interface Presenter {
        fun loadTravels()
        fun addTravel(userId: Int, token: String, travelName: String)
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun unsubscribe()
    }
}
