package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.JsonAddTravelAnswer
import com.github.travelplannerapp.jsondatamodels.JsonAddTravelRequest
import com.google.gson.Gson

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private var travels = listOf<String>()

    override fun loadTravels() {
        view.loadTravels(CommunicationService.serverApi, this::handleResponse)
    }

    override fun addTravel(userId: Int, token: String, travelName: String) {
        val requestBody = Gson().toJson(JsonAddTravelRequest(userId, token, travelName))
        view.addTravel(CommunicationService.serverApi, requestBody, this::handleAddTravelResponse)
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
        val response = Gson().fromJson(jsonString, JsonAddTravelAnswer::class.java)
        view.showAddTravelResult(response.result)
    }
}
