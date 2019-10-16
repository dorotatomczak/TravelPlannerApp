package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.communication.commonmodel.Place

interface PlanElementDetailsContract {

    interface View {
        fun setTitle(title: String)
    }

    interface Presenter {
        fun showPlaceInfo(place: Place)
        fun saveRating(stars: Int)

    }
}