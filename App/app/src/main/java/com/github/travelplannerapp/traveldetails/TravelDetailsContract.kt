package com.github.travelplannerapp.traveldetails

interface TravelDetailsContract {

    interface View {
        fun setTitle(title: String)
        fun setResult(travelId: Int, travelName: String)
        fun showDayPlans(travelId: Int)
        fun showTransport()
        fun showAccommodation()
        fun showScans(travelId: Int)
        fun showEditTravel()
        fun showSnackbar(messageCode: Int)
    }

    interface TileItemView {
        fun setBackgroundColor(tileIndex: Int)
        fun setImage(tileIndex: Int)
        fun setName(tileIndex: Int)
        fun setMinHeight(tileIndex: Int)
    }

    interface Presenter {
        fun loadTravel()
        fun openCategory(category: Category.CategoryType)
        fun changeTravelName(travelName: String)
    }
}
