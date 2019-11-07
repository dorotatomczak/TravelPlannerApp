package com.github.travelplannerapp.traveldetails.addplanelement

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable
import java.lang.Thread.sleep


class AddPlanElementPresenter(private val travelId: Int, view: AddPlanElementContract.View) : BasePresenter<AddPlanElementContract.View>(view), AddPlanElementContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var place: Place? = null
    private var waitForSecondElement = false


    override fun addPlanElement(data: AddPlanElementContract.NewPlanElementData) {
        if (isPlanDataValid(data)) {
            val planElement = PlanElement(-1,
                    DateTimeUtils.stringToDateTime(data.fromDate, data.fromTime).timeInMillis,
                    -1,
                    place!!,
                    data.notes)
            requestAddPlanElement(planElement)

            if (data.accommodationData != null) {
                waitForSecondElement = true
                sleep(500)
                val planElementEnd = PlanElement(-1,
                        DateTimeUtils.stringToDateTime(data.accommodationData.toDate, data.accommodationData.toTime).timeInMillis,
                        -1,
                        place!!,
                        data.notes)
                requestAddPlanElement(planElementEnd)
            }
        }
    }

    override fun onPlaceFound(place: Place) {
        this.place = place
    }

    private fun requestAddPlanElement(planElement: PlanElement) {
        compositeDisposable.add(CommunicationService.serverApi.addPlanElement(SharedPreferencesUtils.getUserId(), travelId, planElement)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { addedPlan -> handleAddDayPlanResponse(addedPlan) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun isPlanDataValid(data: AddPlanElementContract.NewPlanElementData): Boolean {

        if (data.name.isEmpty() || data.fromDate.isEmpty() || data.fromTime.isEmpty()) {
            view.showSnackbar(R.string.missing_required_fields_error)
            return false
        }

        if (place == null) {
            return false
        }

        if (data.accommodationData != null) {
            if (data.accommodationData.toDate.isEmpty() || data.accommodationData.toTime.isEmpty()) {
                view.showSnackbar(R.string.missing_required_fields_error)
                return false
            }

            if (!DateTimeUtils.isDateTimeABeforeDateTimeB(data.fromDate, data.fromTime, data.accommodationData.toDate, data.accommodationData.toTime)) {
                view.showSnackbar(R.string.wrong_dates_error)
                return false
            }
        }

        return true
    }

    private fun handleAddDayPlanResponse(planElement: PlanElement) {
        if(waitForSecondElement) waitForSecondElement = false
        else view.returnResultAndFinish(R.string.add_plan_ok, planElement)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
