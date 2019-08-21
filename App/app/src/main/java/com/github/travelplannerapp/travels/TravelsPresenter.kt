package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.AddTravelRequest
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.jsondatamodels.Travel
import com.github.travelplannerapp.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var travels = ArrayList<Travel>()

    override fun loadTravels(token: String, userId: Int) {
        compositeDisposable.add(CommunicationService.serverApi.getTravels(token, userId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .subscribe(
                        { travels -> handleGetTravelsResponse(travels) },
                        //TODO [Dorota] Display errors from server or server connection failure
                        { view.showSnackbar(R.string.server_connection_failure) }
                ))
    }

    override fun addTravel(userId: Int, token: String, travelName: String) {
        compositeDisposable.add(CommunicationService.serverApi.addTravel(token, AddTravelRequest(userId, travelName))
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .subscribe(
                        { travel -> handleAddTravelResponse(travel) },
                        //TODO [Dorota] Display errors from server or server connection failure
                        { view.showSnackbar(R.string.server_connection_failure) }
                ))
    }

    override fun onBindTravelsAtPosition(position: Int, itemView: TravelsContract.TravelItemView) {
        val travel = travels[position]
        itemView.setName(travel.name)
    }

    override fun getTravelsCount(): Int {
        return travels.size
    }

    override fun openTravelDetails(position: Int) {
        val travel = travels[position]
        view.showTravelDetails(travel.id)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun handleGetTravelsResponse(myTravels: List<Travel>) {
        travels = ArrayList(myTravels)
        view.onDataSetChanged()

        if (travels.isEmpty()) view.showNoTravels() else view.showTravels()
    }

    private fun handleAddTravelResponse(travel: Travel) {
        travels.add(travel)
        view.onDataSetChanged()
    }

}
