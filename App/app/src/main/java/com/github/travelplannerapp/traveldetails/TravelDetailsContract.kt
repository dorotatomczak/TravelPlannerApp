package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.UserInfo
import com.github.travelplannerapp.deleteactionmode.DeleteContract
import java.io.File

interface TravelDetailsContract {

    interface View : DeleteContract.View {
        fun setTitle(title: String)
        fun showImage(url: String)
        fun showDayPlans()
        fun showNoDayPlans()
        fun showAddPlanElement(travelId: Int)
        fun showShareTravel(friendsWithoutAccessToTravel: List<UserInfo>)
        fun onDataSetChanged()
        fun hideLoadingIndicator()
        fun setResult(travel: Travel)
        fun showSnackbar(messageCode: Int)
        fun showPlanElementDetails(planElement: PlanElement, placeTitle: String, travelId: Int)
        fun getAccommodationName(isCheckIn: Boolean, placeTitle: String): String
        fun sharePlanElement(urlToShare: String)
    }

    interface PlanElementItemView : DeleteContract.ItemView {
        fun setName(name: String)
        fun setCompleted(completed: Boolean)
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

    interface Presenter : DeleteContract.Presenter {
        fun loadTravel()
        fun changeTravelName(travelName: String)
        fun shareTravel(selectedFriendsIds: List<Int>)
        fun uploadTravelImage(image: File)
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
        fun markPlanElement(position: Int, isCompleted: Boolean)
        fun updatePlanElement(plan: PlanElement)
        fun deletePlanElements()
        fun onPlanElementClicked(position: Int, placeTitle: String)
        fun sharePlanElement(planElementName: String)
        fun loadFriendsWithoutAccessToTravel()
        fun onShareTravelClicked()
    }
}
