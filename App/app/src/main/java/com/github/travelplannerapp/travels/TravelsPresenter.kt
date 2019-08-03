package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.CommunicationService

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {


    private var travels = listOf<String>()

    override fun loadTravels() {
        val requestInterface = CommunicationService.serverApi

        view.loadTravels(requestInterface, this::handleResponse)
    }

    override fun onBindTravelsAtPosition(position: Int, itemView: TravelsContract.TravelItemView) {
        val travel = travels[position]
        itemView.setName(travel)
    }

    override fun getTravelsCount(): Int {
        return travels.size
    }

    override fun openTravelDetails(position: Int) {
        val travel = travels[position]
        view.showTravelDetails(travel)
    }
    override fun handleResponse(myTravels: List<String>) {

        travels = ArrayList(myTravels)

        if (travels.isEmpty()){
            view.showNoTravels()
        }else{
            view.showTravels()
        }

    }
}
