package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.communication.model.Plan

interface DayPlansContract {
    interface View {
        fun showDayPlans()
        fun showNoDayPlans()
        fun onDataSetChanged()
        fun hideLoadingIndicator()
        fun showSnackbar(messageCode: Int)
    }

    interface PlanElementItemView {
        fun setName(name: String)
        fun setFromTime(time: String)
        fun setIcon(icon: Int)
        fun setLocation(location: String)
        fun showLine()
        fun hideLine()
    }

    interface PlanDateSeparatorItemView {
        fun setDate(date: String)
    }

    interface DayPlanItem {

        fun getType(): Int

        companion object {
            const val TYPE_DATE = 0
            const val TYPE_PLAN = 1
        }
    }

    interface Presenter {
        fun onPlanAdded(plan: Plan)
        fun unsubscribe()
        fun loadDayPlans()
        fun getPlanItemsCount(): Int
        fun getPlanItemType(position: Int): Int
        fun onBindPlanItemAtPosition(position: Int, itemView: PlanElementItemView)
        fun onBindPlanItemAtPosition(position: Int, itemView: PlanDateSeparatorItemView)
        fun getTravelId(): Int
    }
}
