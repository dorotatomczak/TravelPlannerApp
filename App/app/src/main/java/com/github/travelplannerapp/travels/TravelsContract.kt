package com.github.travelplannerapp.travels

interface TravelsContract {
    interface View

    interface TravelItemView {
        fun setName(name: String)
    }

    interface Presenter{

        fun getTravelsCount() : Int

        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)
    }
}
