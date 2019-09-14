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
    private var travelsChecks = BooleanArray(travels.size)

    override fun loadTravels() {
        view.setLoadingIndicatorVisibility(true)

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

    override fun onDeleteClicked() {
        if (getTravelToDeleteIds().size > 0) {
            view.showConfirmationDialog()
        }
    }

    override fun deleteTravels() {
        val travelsToDeleteIds = getTravelToDeleteIds()
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
        itemView.setCheckbox(travelsChecks[position])
    }

    override fun getTravelsCount(): Int {
        return travels.size
    }

    override fun openTravelDetails(position: Int) {
        val travel = travels[position]
        view.showTravelDetails(travel.id, travel.name)
    }

    override fun setTravelCheck(position: Int, checked: Boolean) {
        travelsChecks[position] = checked
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun enterActionMode() {
        view.onDataSetChanged()
        view.showActionMode()
    }

    override fun leaveActionMode() {
        travelsChecks = BooleanArray(travels.size) { false }
        view.onDataSetChanged()
        view.showNoActionMode()
    }

    private fun getTravelToDeleteIds(): ArrayList<Int> {
        val travelsToDeleteIds = ArrayList<Int>()
        travelsChecks.mapIndexed { index, isChecked -> if (isChecked) travelsToDeleteIds.add(travels[index].id) }
        return travelsToDeleteIds
    }

    private fun handleLoadTravelsResponse(myTravels: List<Travel>) {
        travels = ArrayList(myTravels)
        travelsChecks = BooleanArray(travels.size) { false }
        view.onDataSetChanged()
        view.setLoadingIndicatorVisibility(false)
        if (travels.isEmpty()) view.showNoTravels() else view.showTravels()
    }

    private fun handleAddTravelResponse(travel: Travel) {
        travels.add(travel)
        travelsChecks = BooleanArray(travels.size) { false }
        view.showTravels()
        view.onDataSetChanged()
    }

    private fun handleDeleteTravelsResponse() {
        loadTravels()
        view.showSnackbar(R.string.delete_travels_ok)
    }

    private fun handleErrorResponse(error: Throwable) {
        view.setLoadingIndicatorVisibility(false)
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
