package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.appmodel.PlaceCategory
import com.github.travelplannerapp.communication.appmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import kotlin.collections.ArrayList


class DayPlansPresenter(private val travelId: Int, view: DayPlansContract.View) : BasePresenter<DayPlansContract.View>(view), DayPlansContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    private var dayPlanItems = ArrayList<DayPlansContract.DayPlanItem>()
    private var planElements = TreeSet<PlanElement>()
    private var planElementIdsToDelete = mutableSetOf<Int>()

    override fun onAddPlanElementClicked() {
        view.showAddPlanElement(travelId)
    }

    override fun onPlanElementAdded(planElement: PlanElement) {
        planElements.add(planElement)
        planElementsToDayPlanItems()

        view.showDayPlans()
        view.onDataSetChanged()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun loadDayPlans() {
        compositeDisposable.add(CommunicationService.serverApi.getPlanElements(SharedPreferencesUtils.getUserId(), travelId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { planElements -> handleLoadDayPlansResponse(planElements) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun getDayPlanItemsCount(): Int {
        return dayPlanItems.size
    }

    override fun getDayPlanItemType(position: Int): Int {
        return dayPlanItems[position].getType()
    }

    override fun onBindDayPlanItemAtPosition(position: Int, itemView: DayPlansContract.PlanElementItemView) {
        val planElementItem = dayPlanItems[position] as PlanElementItem
        val planElement = planElementItem.planElement

        if (position + 1 < dayPlanItems.size && dayPlanItems[position+1].getType() == DayPlansContract.DayPlanItem.TYPE_PLAN) {
            itemView.showLine()
        } else {
            itemView.hideLine()
        }

        val categoryIcon = PlaceCategory.values()[planElement.place.categoryIcon].categoryIcon
        val fromTime = DateTimeUtils.timeToString(planElement.fromDateTimeMs)

        itemView.setName(planElement.place.title)
        itemView.setFromTime(fromTime)
        itemView.setLocation(planElement.place.vicinity)
        itemView.setIcon(categoryIcon)
        itemView.setCheckbox()
    }

    override fun onBindDayPlanItemAtPosition(position: Int, itemView: DayPlansContract.DateSeparatorItemView) {
        val dateSeparatorItem = dayPlanItems[position] as DateSeparatorItem
        val date = dateSeparatorItem.date

        itemView.setDate(date)
    }

    override fun addPlanElementIdToDelete(position: Int) {
        val planElementItem = dayPlanItems[position] as PlanElementItem
        planElementIdsToDelete.add(planElementItem.planElement.id)
    }

    override fun removePlanElementIdToDelete(position: Int) {
        val planElementItem = dayPlanItems[position] as PlanElementItem
        planElementIdsToDelete.remove(planElementItem.planElement.id)
    }

    override fun deletePlanElements() {
        compositeDisposable.add(CommunicationService.serverApi.deletePlanElements(
                SharedPreferencesUtils.getUserId(),
                planElementIdsToDelete.toList())
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { handleDeletePlanElementsResponse() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun onDeleteClicked() {
        if (planElementIdsToDelete.size > 0) {
            view.showConfirmationDialog()
        }
    }

    override fun enterActionMode() {
        view.onDataSetChanged()
        view.showActionMode()
    }

    override fun leaveActionMode() {
        view.onDataSetChanged()
        view.showNoActionMode()
    }

    private fun planElementsToDayPlanItems() {
        dayPlanItems = ArrayList()
        var date = ""

        for (plan in planElements) {
            val dateCursor = DateTimeUtils.dateToString(plan.fromDateTimeMs)
            if (date != dateCursor) {
                date = dateCursor
                dayPlanItems.add(DateSeparatorItem(date))
            }
            dayPlanItems.add(PlanElementItem(plan))
        }
    }

    inner class DateSeparatorItem(val date: String) : DayPlansContract.DayPlanItem {
        override fun getType(): Int {
            return DayPlansContract.DayPlanItem.TYPE_DATE
        }
    }

    inner class PlanElementItem(val planElement: PlanElement) : DayPlansContract.DayPlanItem {
        override fun getType(): Int {
            return DayPlansContract.DayPlanItem.TYPE_PLAN
        }
    }

    private fun handleLoadDayPlansResponse(planElements: List<PlanElement>) {
        this.planElements = TreeSet()
        this.planElements.addAll(planElements)
        planElementsToDayPlanItems()
        view.onDataSetChanged()
        view.hideLoadingIndicator()

        if (this.dayPlanItems.isEmpty()) view.showNoDayPlans() else view.showDayPlans()
    }

    private fun handleDeletePlanElementsResponse() {
        planElementIdsToDelete.clear()
        loadDayPlans()
        view.showSnackbar(R.string.delete_plan_elements_ok)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
