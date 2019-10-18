package com.github.travelplannerapp.traveldetails.addplanelement

import com.github.travelplannerapp.communication.appmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.Place

interface AddPlanElementContract {

    data class Coordinates(var latitude: Double, var longitude: Double)

    data class NewPlanElementData(val name: String, val fromDate: String, val fromTime: String,
                                  val coordinates: Coordinates, val location: String)

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
