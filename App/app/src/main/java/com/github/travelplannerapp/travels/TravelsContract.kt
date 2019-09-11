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
        fun hideFabAdd()
        fun showFabAdd()
    }

    interface TravelItemView {
        fun setName(name: String)
        fun setCheckbox()
        fun leaveActionMode()
    }

    interface Presenter {
        fun loadTravels()
        fun addTravel(travelName: String)
        fun deleteTravels()
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun addPositionToDelete(position: Int)
        fun removePositionToDelete(position: Int)
        fun unsubscribe()
        fun onActionModeEnter()
        fun onActionModeLeave()
    }
}
