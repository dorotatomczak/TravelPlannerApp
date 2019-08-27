package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.model.AddTravelRequest
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.communication.model.Travel
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var travels = ArrayList<Travel>()

    override fun loadTravels(token: String, userId: Int) {
        compositeDisposable.add(CommunicationService.serverApi.getTravels(token, userId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { travels -> handleLoadTravelsResponse(travels) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun addTravel(userId: Int, token: String, travelName: String) {
        compositeDisposable.add(CommunicationService.serverApi.addTravel(token, AddTravelRequest(userId, travelName))
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { travel -> handleAddTravelResponse(travel) },
                        { error -> handleErrorResponse(error) }
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
        view.showTravelDetails(travel.id, travel.name)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    private fun handleLoadTravelsResponse(myTravels: List<Travel>) {
        travels = ArrayList(myTravels)
        view.onDataSetChanged()

        if (travels.isEmpty()) view.showNoTravels() else view.showTravels()
    }

    private fun handleAddTravelResponse(travel: Travel) {
        travels.add(travel)
        view.showTravels()
        view.onDataSetChanged()
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

}
