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

    override fun search(category: String, west: String, south: String, east: String, north: String) {

            compositeDisposable.add(CommunicationService.serverApi.findFacilities(category, west, south, east, north)
                    .observeOn(SchedulerProvider.ui())
                    .subscribeOn(SchedulerProvider.io())
                    .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                    .subscribe(
                            { handleSearchResponse() },
                            { error -> handleErrorResponse(error) }
                    ))
        }

    private fun handleSearchResponse() {
        view.returnResultAndFinish(R.string.sign_up_successful)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

}