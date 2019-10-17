package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.PlaceData
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class PlanElementDetailsPresenter(view: PlanElementDetailsContract.View) : BasePresenter<PlanElementDetailsContract.View>(view), PlanElementDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var averageRating: String? = null

    override fun showPlaceInfo(placeHref: String) {
        loadPlace(placeHref)        
    }

    override fun saveRating(stars: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

    private fun loadPlace(placeHref: String) {
        compositeDisposable.add(CommunicationService.serverApi.getPlace("0", placeHref)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { place -> handleLoadPlaceResponse(place) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadPlaceResponse(placeData: PlaceData) {
        view.showName(placeData.name ?: "")
        view.showLocation(placeData.location?.address?.text ?: "")
        view.showOpeningHours(placeData.extended?.openingHours?.text ?: "")
        view.showAverageRating(averageRating ?: "0.0")
        view.showContacts(placeData.contacts!!)

        view.showProgressIndicator(false)
        view.showInfoLayout(true)
    }

    override fun setAverageRating(rating: String) {
        averageRating = rating
    }

}