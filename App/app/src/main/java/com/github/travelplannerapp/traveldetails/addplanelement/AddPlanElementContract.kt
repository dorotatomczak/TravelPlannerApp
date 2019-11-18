package com.github.travelplannerapp.traveldetails.addplanelement

import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement

interface AddPlanElementContract {

    data class Coordinates(var latitude: Double = 0.0, var longitude: Double = 0.0)

    data class NewPlanElementData(val name: String, val fromDate: String, val fromTime: String,
                                  val coordinates: Coordinates, val location: String,
                                  val accommodationData: AccommodationData?, val notes: String)

    data class AccommodationData(val toDate: String, val toTime: String)

    interface View {
        fun showLocation(location: String)
        fun showSnackbar(messageCode: Int)
        fun returnResultAndFinish(messageCode: Int, planElement: PlanElement)
    }

    interface Presenter {
        fun addPlanElement(data: NewPlanElementData)
        fun onPlaceFound(place: Place)
    }
}
