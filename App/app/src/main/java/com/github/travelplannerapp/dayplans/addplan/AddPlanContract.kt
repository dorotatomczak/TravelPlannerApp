package com.github.travelplannerapp.dayplans.addplan

interface AddPlanContract {

    data class NewPlanData(val name: String, val fromDate: String, val fromTime: String,
                                 val toDate: String, val toTime: String)

    interface View {
        fun showLocation(location: String)
        fun showSnackbar(messageCode: Int)
    }

    interface Presenter {
        fun addPlan(data: NewPlanData)
    }
}
