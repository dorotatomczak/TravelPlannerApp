package com.github.travelplannerapp.dayplans.addplanelement

import com.github.travelplannerapp.communication.appmodel.PlaceCategory
import com.github.travelplannerapp.communication.commonmodel.PlanElement

interface AddPlanElementContract {

    data class Coordinates(var lattitude: Double, var longitude: Double)

    data class NewPlanElementData(val category: PlaceCategory, val name: String, val fromDate: String, val fromTime: String,
                                  val coordinates: Coordinates, val location: String)

    interface View {
        fun showLocation(location: String)
        fun showSnackbar(messageCode: Int)
        fun returnResultAndFinish(messageCode: Int, planElement: PlanElement)
    }

    interface Presenter {
        fun addPlanElement(data: NewPlanElementData)
        fun onPlaceFound(placeId: String, href: String)
    }
}
