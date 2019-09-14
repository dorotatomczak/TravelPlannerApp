package com.github.travelplannerapp.dayplans.addplan

import com.github.travelplannerapp.communication.model.PlaceCategory
import com.github.travelplannerapp.communication.model.Plan

interface AddPlanContract {

    data class Coordinates(var lattitude: Double, var longitude: Double)

    data class NewPlanData(val category: PlaceCategory, val name: String, val fromDate: String, val fromTime: String,
                                 val toDate: String, val toTime: String, val coordinates: Coordinates, val location: String)

    interface View {
        fun showLocation(location: String)
        fun showSnackbar(messageCode: Int)
        fun returnResultAndFinish(messageCode: Int, plan: Plan)
    }

    interface Presenter {
        fun addPlan(data: NewPlanData)
    }
}
