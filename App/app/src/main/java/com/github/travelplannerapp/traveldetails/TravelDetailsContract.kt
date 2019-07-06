package com.github.travelplannerapp.traveldetails

interface TravelDetailsContract {

    interface View {
        fun setTitle(title: String)
        fun showDayPlans()
        fun showTransport()
        fun showAccommodation()
        fun showTickets()
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
    }
}