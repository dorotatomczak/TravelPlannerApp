package com.github.travelplannerapp.travels

import com.github.travelplannerapp.deleteactionmode.DeleteContract


interface TravelsContract {
    interface View : DeleteContract.View {
        fun showAddTravel()
        fun showTravelDetails(travelId: Int, travelName: String)
        fun showTravels()
        fun showNoTravels()
        fun showSnackbar(messageCode: Int)
        fun showSnackbar(message: String)
        fun onDataSetChanged()
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
    }

    interface TravelItemView : DeleteContract.ItemView {
        fun setName(name: String)
    }

    interface Presenter : DeleteContract.Presenter {
        fun loadTravels()
        fun addTravel(travelName: String)
        fun deleteTravels()
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun addTravelToDeleteId(position: Int)
        fun removeTravelToDeleteId(position: Int)
        fun unsubscribe()
    }
}
