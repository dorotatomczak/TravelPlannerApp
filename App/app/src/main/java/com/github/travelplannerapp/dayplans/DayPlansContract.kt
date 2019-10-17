package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.deleteactionmode.DeleteContract

interface DayPlansContract {
    interface View : DeleteContract.View{
        fun showDayPlans()
        fun showNoDayPlans()
        fun showAddPlanElement(travelId: Int)
        fun onDataSetChanged()
        fun hideLoadingIndicator()
        fun showSnackbar(messageCode: Int)
        fun showPlanElementDetails(place: Place, rating: Int)
    }

    interface PlanElementItemView : DeleteContract.ItemView{
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

    interface Presenter : DeleteContract.Presenter{
        fun onAddPlanElementClicked()
        fun onPlanElementAdded(planElement: PlanElement)
        fun unsubscribe()
        fun loadDayPlans()
        fun getDayPlanItemsCount(): Int
        fun getDayPlanItemType(position: Int): Int
        fun onBindDayPlanItemAtPosition(position: Int, itemView: PlanElementItemView)
        fun onBindDayPlanItemAtPosition(position: Int, itemView: DateSeparatorItemView)
        fun addPlanElementIdToDelete(position: Int)
        fun removePlanElementIdToDelete(position: Int)
        fun deletePlanElements()
        fun openPlanElementDetails(position: Int)
        fun saveRating(rating: Int)
    }
}
