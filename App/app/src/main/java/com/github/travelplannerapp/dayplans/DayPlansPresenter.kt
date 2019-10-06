package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.appmodel.PlaceCategory
import com.github.travelplannerapp.communication.commonmodel.Plan
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable


class DayPlansPresenter(private val travelId: Int, view: DayPlansContract.View) : BasePresenter<DayPlansContract.View>(view), DayPlansContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    private var planItems = ArrayList<DayPlansContract.DayPlanItem>()
    private val plans = ArrayList<Plan>()

    override fun onAddPlanClicked() {
        view.showAddPlan(travelId)
    }

    override fun onPlanAdded(plan: Plan) {
        plans.add(plan)
        plansToDayPlanItems(plans)
        view.showDayPlans()
        view.onDataSetChanged()

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadDayPlans() {
        compositeDisposable.add(CommunicationService.serverApi.getPlans(SharedPreferencesUtils.getUserId(), travelId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { plans -> handleLoadDayPlansResponse(plans) },
                        { error -> handleErrorResponse(error) }
                ))

    }

    override fun getPlanItemsCount(): Int {
        return planItems.size
    }

    override fun getPlanItemType(position: Int): Int {
        return planItems[position].getType()
    }

    override fun onBindPlanItemAtPosition(position: Int, itemView: DayPlansContract.PlanElementItemView) {
        val planElementItem = planItems[position] as PlanElementItem
        val plan = planElementItem.plan

        if (position + 1 < planItems.size && planItems[position + 1].getType() == DayPlansContract.DayPlanItem.TYPE_PLAN) {
            itemView.showLine()
        }

        val categoryIcon = PlaceCategory.values()[plan.place.categoryIcon].categoryIcon
        val fromTime = DateTimeUtils.timeToString(plan.fromDateTimeMs)

        itemView.setName(plan.place.title)
        itemView.setFromTime(fromTime)
        itemView.setLocation(plan.place.vicinity)
        itemView.setIcon(categoryIcon)
    }

    override fun onBindPlanItemAtPosition(position: Int, itemView: DayPlansContract.PlanDateSeparatorItemView) {
        val dateSeparatorItem = planItems[position] as DateSeparatorItem
        val date = dateSeparatorItem.date

        itemView.setDate(date)
    }

    private fun plansToDayPlanItems(plans: List<Plan>) {

        planItems = ArrayList()
        var date = ""

        for (plan in plans) {
            val dateCursor = DateTimeUtils.dateToString(plan.fromDateTimeMs)
            if (date != dateCursor) {
                date = dateCursor
                planItems.add(DateSeparatorItem(date))
            }
            planItems.add(PlanElementItem(plan))
        }
    }

    inner class DateSeparatorItem(val date: String) : DayPlansContract.DayPlanItem {
        override fun getType(): Int {
            return DayPlansContract.DayPlanItem.TYPE_DATE
        }
    }

    inner class PlanElementItem(val plan: Plan) : DayPlansContract.DayPlanItem {
        override fun getType(): Int {
            return DayPlansContract.DayPlanItem.TYPE_PLAN
        }
    }

    private fun handleLoadDayPlansResponse(plans: List<Plan>) {
        plansToDayPlanItems(plans)
        view.onDataSetChanged()
        view.hideLoadingIndicator()

        if (this.planItems.isEmpty()) view.showNoDayPlans() else view.showDayPlans()
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
