package com.github.travelplannerapp.traveldetails.addtransport

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlaceData
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.commonmodel.Routes
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AddTransportPresenter(private var from: Place,
                            private var to: Place,
                            private var departureDate: Long,
                            private val travelId: Int, view: AddTransportContract.View) : BasePresenter<AddTransportContract.View>(view), AddTransportContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var travelMode = "driving"

    override fun initFromToTransport() {
        view.setFromTransport(from.title)
        from.position = arrayOf(0.0,0.0)
        view.setToTransport(to.title)
        to.position = arrayOf(0.0,0.0)
        view.setDepartureDate(Date(departureDate).toString())
    }

    override fun onAddTransportClicked() {
        loadFromPlaceHereData(from.href)
        loadToPlaceHereData(to.href)
    }

    override fun onTravelModeSelected(mode: String) {
        travelMode = mode
    }

    private fun loadFromPlaceHereData(href: String) {
        compositeDisposable.add(CommunicationService.serverApi.getPlace("0", href)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { place -> handleLoadFromPlaceHereDataResponse(place) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadFromPlaceHereDataResponse(placeData: PlaceData) {
        from.position = placeData.location!!.position

        if (from.position[0] != 0.0 && to.position[0] != 0.0) {
            loadTransport()
        }
    }

    private fun loadToPlaceHereData(href: String) {
        compositeDisposable.add(CommunicationService.serverApi.getPlace("0", href)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { place -> handleLoadToPlaceHereDataResponse(place) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadToPlaceHereDataResponse(placeData: PlaceData) {
        to.position = placeData.location!!.position

        if (from.position[0] != 0.0 && to.position[0] != 0.0) {
            loadTransport()
        }
    }

    private fun loadTransport() {
        compositeDisposable.add(CommunicationService.serverApi.getTransport(
                from.position[0].toString(),
                from.position[1].toString(),
                to.position[0].toString(),
                to.position[1].toString(),
                travelMode,
                departureDate.toString()
        )
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { routes -> handleLoadTransportResponse(routes) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadTransportResponse(routes: Routes) {
        view.showTransportResult(routes)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
