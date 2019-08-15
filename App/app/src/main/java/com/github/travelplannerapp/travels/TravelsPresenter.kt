package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonAddTravelAnswer
import com.github.travelplannerapp.jsondatamodels.JsonAddTravelRequest
import com.google.gson.Gson

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private var travels = listOf<String>()

    override fun loadTravels() {
        val requestInterface = CommunicationService.serverApi

        view.loadTravels(requestInterface, this::handleResponse)
    }

    override fun addTravel(email: String, auth: String, travelName: String) {
        val requestInterface = CommunicationService.serverApi
        val requestBody = Gson().toJson(JsonAddTravelRequest(email, auth, travelName))

        view.addTravel(requestInterface, requestBody, this::handleAddTravelResponse)
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

        if (travels.isEmpty()) {
            view.showNoTravels()
        } else {
            view.showTravels()
        }
    }

    override fun handleAddTravelResponse(jsonString: String) {
        val answer = Gson().fromJson(jsonString, JsonAddTravelAnswer::class.java)
        view.showAddTravelResult(answer.result)
    }
}
