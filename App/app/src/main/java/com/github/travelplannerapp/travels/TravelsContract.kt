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
        fun setLoadingIndicatorVisibility(isVisible: Boolean)
        fun showActionMode()
        fun showNoActionMode()
        fun showConfirmationDialog()
    }

    interface TravelItemView {
        fun setName(name: String)
        fun setCheckbox(checked: Boolean)
    }

    interface Presenter {
        fun loadTravels()
        fun addTravel(travelName: String)
        fun onDeleteClicked()
        fun deleteTravels()
        fun getTravelsCount(): Int
        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
        fun openTravelDetails(position: Int)
        fun setTravelCheck(position: Int, checked: Boolean)
        fun unsubscribe()
        fun enterActionMode()
        fun leaveActionMode()
    }
}
