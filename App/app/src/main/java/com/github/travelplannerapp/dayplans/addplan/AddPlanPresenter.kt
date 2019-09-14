package com.github.travelplannerapp.dayplans.addplan

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.model.Place
import com.github.travelplannerapp.communication.model.Plan
import com.github.travelplannerapp.utils.DateTimeUtils
import java.util.*

class AddPlanPresenter(view: AddPlanContract.View) : BasePresenter<AddPlanContract.View>(view), AddPlanContract.Presenter {

    override fun addPlan(data: AddPlanContract.NewPlanData) {
        if (isPlanDataValid(data)) {
            //TODO [Dorota] Send to server

            val place = Place(data.name, arrayOf(data.coordinates.lattitude, data.coordinates.longitude), data.location, data.category)

            val plan = Plan(
                    Locale.getDefault(),
                    DateTimeUtils.stringToDateTime(data.fromDate, data.fromTime),
                    DateTimeUtils.stringToDateTime(data.toDate, data.toTime),
                    place)

            view.returnResultAndFinish(R.string.add_plan_ok, plan)
        }
    }

    private fun isPlanDataValid(data: AddPlanContract.NewPlanData) : Boolean {

        if (data.name.isEmpty() || data.fromDate.isEmpty() || data.fromTime.isEmpty() ||
                data.toDate.isEmpty() || data.toTime.isEmpty()) {
            view.showSnackbar(R.string.missing_required_fields_error)
            return false
        }

        if (!DateTimeUtils.isDateTimeABeforeDateTimeB(data.fromDate, data.fromTime, data.toDate, data.toTime)) {
            view.showSnackbar(R.string.wrong_dates_error)
            return false
        }

        return true
    }
}
