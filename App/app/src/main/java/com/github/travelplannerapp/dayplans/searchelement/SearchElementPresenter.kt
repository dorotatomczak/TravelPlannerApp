package com.github.travelplannerapp.dayplans.searchelement

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.SchedulerProvider
import com.here.android.mpa.mapping.MapMarker
import io.reactivex.disposables.CompositeDisposable

class SearchElementPresenter(view: SearchElementContract.View) : BasePresenter<SearchElementContract.View>(view), SearchElementContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val placesMap = mutableMapOf<String, Place>()

    override fun search(category: String, west: String, north: String, east: String, south: String) {
        if (west != "0.0"
                || north != "0.0"
                || east != "0.0"
                || south != "0.0") {
        compositeDisposable.add(CommunicationService.serverApi.findObjects(category, west, north, east, south)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                .subscribe(
                        { places -> view.loadObjectsOnMap(places!!) },
                        { error -> handleErrorResponse(error) }
                ))
    }
    }

    override fun clearPlacesMap() {
        placesMap.clear()
    }

    override fun getPlace(marker: MapMarker): Place {
        return placesMap[createKeyFromMarker(marker)]!!
    }

    override fun setContacts(objectId: String, href: String) {
        compositeDisposable.add(CommunicationService.serverApi.getContacts(objectId, href)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data else throw ApiException(it.responseCode) }
                .subscribe(
                        { contacts -> view.showContacts(contacts!!) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun savePlaceInMap(marker: MapMarker, place: Place) {
        placesMap[createKeyFromMarker(marker)] = place
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

    private fun createKeyFromMarker(marker: MapMarker): String {
        return marker.title + marker.description
    }
}
