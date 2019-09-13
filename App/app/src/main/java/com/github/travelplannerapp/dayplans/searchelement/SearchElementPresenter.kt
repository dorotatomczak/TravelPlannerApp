package com.github.travelplannerapp.dayplans.searchelement

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SearchElementPresenter (view: SearchElementContract.View) : BasePresenter<SearchElementContract.View>(view), SearchElementContract.Presenter{
    private val compositeDisposable = CompositeDisposable()

    override fun search(category: String, west: String, north: String, east: String, south: String) {

            compositeDisposable.add(CommunicationService.serverApi.findFacilities(category, west, north, east, south)
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                    .subscribe(
                            { places -> view.loadObjectsOnMap(places!!)},
                            { error -> handleErrorResponse(error) }
                    ))
        }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}