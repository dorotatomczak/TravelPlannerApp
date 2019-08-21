package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.BasePresenter

class TravelDetailsPresenter (private var travelId: Int, view: TravelDetailsContract.View): BasePresenter<TravelDetailsContract.View>(view), TravelDetailsContract.Presenter {

    override fun loadTravel() {
        //TODO [Dorota] load travel and set travel name
        view.setTitle(travelId.toString())
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