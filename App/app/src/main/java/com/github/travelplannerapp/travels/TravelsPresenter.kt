package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.communication.model.Travel
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var travels = ArrayList<Travel>()
    var travelsToDeleteIds = ArrayList<Int>()

    override fun loadTravels() {
        compositeDisposable.add(CommunicationService.serverApi.getTravels()
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { travels -> handleLoadTravelsResponse(travels) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun addTravel(travelName: String) {
        compositeDisposable.add(CommunicationService.serverApi.addTravel(travelName)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { travel -> handleAddTravelResponse(travel) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun deleteTravels() {
        compositeDisposable.add(CommunicationService.serverApi.deleteTravels(travelsToDeleteIds)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { handleDeleteTravelsResponse() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun onBindTravelsAtPosition(position: Int, itemView: TravelsContract.TravelItemView) {
        val travel = travels[position]
        itemView.setName(travel.name)
        itemView.setCheckbox()
    }

    override fun getTravelsCount(): Int {
        return travels.size
    }

    override fun openTravelDetails(position: Int) {
        val travel = travels[position]
        view.showTravelDetails(travel.id, travel.name)
    }

    override fun addPositionToDelete(position: Int) {
        travelsToDeleteIds.add(travels[position].id)
    }

    override fun removePositionToDelete(position: Int) {
        travelsToDeleteIds.remove(travels[position].id)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun enterActionMode() {
        view.onDataSetChanged()
        view.showActionMode()
    }

    override fun leaveActionMode() {
        view.onDataSetChanged()
        view.showNoActionMode()
    }

    private fun handleLoadTravelsResponse(myTravels: List<Travel>) {
        travels = ArrayList(myTravels)
        view.onDataSetChanged()
        view.hideLoadingIndicator()

        if (travels.isEmpty()) view.showNoTravels() else view.showTravels()
    }

    private fun handleAddTravelResponse(travel: Travel) {
        travels.add(travel)
        view.showTravels()
        view.onDataSetChanged()
    }

    private fun handleDeleteTravelsResponse() {
        travelsToDeleteIds = ArrayList<Int>()
        loadTravels()
    }

    private fun handleErrorResponse(error: Throwable) {
        view.hideLoadingIndicator()
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
