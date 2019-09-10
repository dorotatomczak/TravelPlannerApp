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
    private var travelsToDeletePositions = ArrayList<Int>()

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
        val toDeleteTravelIds = ArrayList<Int>()
        for(position in travelsToDeletePositions){
            toDeleteTravelIds.add(travels[position].id)
        }
        view.showSnackbar(toDeleteTravelIds.toString())

        compositeDisposable.add(CommunicationService.serverApi.deleteTravels(toDeleteTravelIds)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { handleDeleteTravelResponse() },
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
        travelsToDeletePositions.add(position)
    }

    override fun removePositionToDelete(position: Int) {
        travelsToDeletePositions.remove(position)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun onActionModeOnOff() {
        view.onDataSetChanged()
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

    private fun handleDeleteTravelResponse() {
        loadTravels()
    }

    private fun handleErrorResponse(error: Throwable) {
        view.hideLoadingIndicator()
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
