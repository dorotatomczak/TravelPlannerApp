package com.github.travelplannerapp.travels

import com.github.travelplannerapp.actionmodewithdelete.DeletableElementsContract


interface TravelsContract {
    interface View : DeletableElementsContract.View {
        fun showAddTravel()
        fun showTravelDetails(travelId: Int, travelName: String)
        fun showTravels()
        fun showNoTravels()
        fun showSnackbar(messageCode: Int)
        fun showSnackbar(message: String)
        fun onDataSetChanged()
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
    }

    interface TravelItemView : DeletableElementsContract.ItemView {
        fun setName(name: String)
    }

    interface Presenter : DeletableElementsContract.Presenter {
        fun loadTravels()
        fun addTravel(travelName: String)
        fun deleteTravels()
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun unsubscribe()
    }
}
