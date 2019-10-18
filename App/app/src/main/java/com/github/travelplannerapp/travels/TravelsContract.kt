package com.github.travelplannerapp.travels

import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.deleteactionmode.DeleteContract

interface TravelsContract {
    interface View : DeleteContract.View {
        fun showAddTravel()
        fun showTravelDetails(travel: Travel)
        fun showTravels()
        fun showNoTravels()
        fun showSnackbar(messageCode: Int)
        fun showSnackbar(message: String)
        fun onDataSetChanged()
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
    }

    interface TravelItemView : DeleteContract.ItemView {
        fun setName(name: String)
        fun setImage(url: String)
    }

    interface Presenter : DeleteContract.Presenter {
        fun loadTravels()
        fun addTravel(travelName: String)
        fun deleteTravels()
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun addTravelIdToDelete(position: Int)
        fun removeTravelIdToDelete(position: Int)
        fun unsubscribe()
        fun updateTravel(travel: Travel)
    }
}
