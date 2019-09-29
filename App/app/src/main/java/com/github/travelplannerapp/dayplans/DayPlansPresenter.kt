package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.Plan
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class DayPlansPresenter(view: DayPlansContract.View) : BasePresenter<DayPlansContract.View>(view), DayPlansContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var planItems = ArrayList<DayPlansContract.DayPlanItem>()
    private val plans = ArrayList<Plan>()

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
        compositeDisposable.add(CommunicationService.serverApi.getPlans()
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { plans ->  handleLoadDayPlansResponse(plans) },
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

        if (position + 1 < planItems.size && planItems[position+1].getType() == DayPlansContract.DayPlanItem.TYPE_PLAN) {
            itemView.showLine()
        }
        itemView.setName(plan.place.title)
        itemView.setFromTime(DateTimeUtils.timeToString(plan.fromDateTime))
        itemView.setLocation(plan.place.vicinity)
        itemView.setIcon(plan.place.category.categoryIcon)
    }

    override fun onBindPlanItemAtPosition(position: Int, itemView: DayPlansContract.PlanDateSeparatorItemView) {
        val dateSeparatorItem = planItems[position] as DateSeparatorItem
        val date = dateSeparatorItem.date

        itemView.setDate(date)
    }

    private fun handleLoadDayPlansResponse(plans: List<Plan>) {
        plansToDayPlanItems(plans)
        view.onDataSetChanged()
        view.hideLoadingIndicator()

        if (this.planItems.isEmpty()) view.showNoDayPlans() else view.showDayPlans()
    }

    private fun plansToDayPlanItems(plans: List<Plan>) {

        planItems = ArrayList()
        var date = ""

        for (plan in plans) {
            val dateCursor = DateTimeUtils.dateToString(plan.fromDateTime)
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

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
