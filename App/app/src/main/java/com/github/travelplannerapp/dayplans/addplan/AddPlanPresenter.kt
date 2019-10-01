package com.github.travelplannerapp.dayplans.addplan

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ObjectCategory
import com.github.travelplannerapp.communication.model.Place
import com.github.travelplannerapp.communication.model.Plan
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AddPlanPresenter(private val travelId: Int, view: AddPlanContract.View) : BasePresenter<AddPlanContract.View>(view), AddPlanContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var placeHereId: String
    private lateinit var href: String


    override fun addPlan(data: AddPlanContract.NewPlanData) {
        if (isPlanDataValid(data)) {
            val place = Place(placeHereId, data.name, data.location, arrayOf(data.coordinates.lattitude, data.coordinates.longitude),
                    ObjectCategory("", data.category.categoryName), href)

            val plan = Plan(-1,
                    Locale.getDefault().toString(),
                    DateTimeUtils.stringToDateTime(data.fromDate, data.fromTime).timeInMillis,
                    DateTimeUtils.stringToDateTime(data.toDate, data.toTime).timeInMillis,
                    -1,
                    place)

            compositeDisposable.add(CommunicationService.serverApi.addPlan(travelId, plan)
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                    .subscribe(
                            { addedPlan -> handleAddDayPlanResponse(addedPlan) },
                            { error -> handleErrorResponse(error) }
                    ))
        }
    }

    override fun savePlaceInfo(placeId: String, href: String) {
        this.placeHereId = placeId
        this.href = href
    }

    private fun isPlanDataValid(data: AddPlanContract.NewPlanData): Boolean {

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

    private fun handleAddDayPlanResponse(plan: Plan) {
        view.returnResultAndFinish(R.string.add_plan_ok, plan)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
