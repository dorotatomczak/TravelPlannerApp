package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.BasePresenter

class TravelDetailsPresenter (private var travelId: String, view: TravelDetailsContract.View): BasePresenter<TravelDetailsContract.View>(view), TravelDetailsContract.Presenter {

    override fun loadTravel() {
        view.setTitle(travelId)
    }

    override fun openCategory(category: Category.CategoryType) {
        when (category) {
            Category.CategoryType.DAY_PLANS -> view.showDayPlans()
            Category.CategoryType.TRANSPORT -> view.showTransport()
            Category.CategoryType.ACCOMMODATION -> view.showAccommodation()
            Category.CategoryType.TICKETS -> view.showTickets()
        }
    }
}