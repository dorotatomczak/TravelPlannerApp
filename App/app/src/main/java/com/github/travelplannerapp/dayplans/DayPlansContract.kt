package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.communication.commonmodel.PlanElement

interface DayPlansContract {
    interface View {
        fun showDayPlans()
        fun showNoDayPlans()
        fun showAddPlanElement(travelId: Int)
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

    interface DateSeparatorItemView {
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
        fun onAddPlanElementClicked()
        fun onPlanElementAdded(planElement: PlanElement)
        fun unsubscribe()
        fun loadDayPlans()
        fun getDayPlanItemsCount(): Int
        fun getDayPlanItemType(position: Int): Int
        fun onBindDayPlanItemAtPosition(position: Int, itemView: PlanElementItemView)
        fun onBindDayPlanItemAtPosition(position: Int, itemView: DateSeparatorItemView)
    }
}
