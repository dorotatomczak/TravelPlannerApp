package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.PlaceData
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class PlanElementDetailsPresenter(view: PlanElementDetailsContract.View) : BasePresenter<PlanElementDetailsContract.View>(view), PlanElementDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var averageRating: String
    private var placeId = 0
    private var changedRating = false

    override fun showPlaceInfo(placeHref: String) {
        loadPlace(placeHref)
    }

    override fun isRatingChanged(): Boolean {
        return changedRating
    }

    override fun saveRating(stars: Int, chosenPlaceId: Int) {
        placeId = chosenPlaceId
        compositeDisposable.add(CommunicationService.serverApi.ratePlace(placeId, stars)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        {
                            changedRating = true
                            view.changeRatingTextToCompleted()
                        },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun setAverageRating(rating: String) {
        averageRating = rating
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
        showPlaceData(placeData)
        view.showProgressIndicator(false)
        view.showInfoLayout(true)
    }

    private fun showPlaceData(placeData: PlaceData) {
        view.showName(placeData.name ?: "")
        view.showLocation(placeData.location?.address?.text ?: "")
        view.showOpeningHours(placeData.extended?.openingHours?.text ?: "")
        view.showAverageRating(averageRating)
        view.showContacts(placeData.contacts!!)
    }
}
