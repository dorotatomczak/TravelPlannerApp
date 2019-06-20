package com.github.travelplannerapp.travels

interface TravelsContract {
    interface View {
        fun showAddTravel()
        //TODO [Dorota] Change to int (travel id) after database is implemented
        fun showTravelDetails(travel: String)
    }

    interface TravelItemView {
        fun setName(name: String)
    }

    interface Presenter{

        fun getTravelsCount() : Int

        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)

        fun openTravelDetails(position: Int)
    }
}
