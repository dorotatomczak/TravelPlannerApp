package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.PlaceData
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class PlanElementDetailsPresenter(private val planElement: PlanElement, private val placeId: Int,
                                  private val placeName: String, private val placeHref: String,
                                  private val placeAverageRating: String, private val travelId: Int,
                                  view: PlanElementDetailsContract.View)
    : BasePresenter<PlanElementDetailsContract.View>(view), PlanElementDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun loadPlace() {
        view.showName(placeName)
        view.showAverageRating(placeAverageRating)
        view.showNotes(planElement.notes ?: "")

        loadPlaceHereData()
        loadPlaceRating()
    }

    override fun onRatingChanged(rating: Int) {
        compositeDisposable.add(CommunicationService.serverApi.ratePlace(SharedPreferencesUtils.getUserId(), placeId, rating)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { view.changeRatingTextToCompleted() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun updatePlanElement(newNotes: String) {
        planElement.notes = newNotes
        compositeDisposable.add(CommunicationService.serverApi.updatePlanElement(SharedPreferencesUtils.getUserId(), travelId, planElement)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { view.showSnackbar(R.string.update_plan_ok) },
                        { error -> handleErrorResponse(error) }))
    }

    private fun loadPlaceHereData() {
        compositeDisposable.add(CommunicationService.serverApi.getPlace("0", placeHref)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { place -> handleLoadPlaceHereDataResponse(place) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun loadPlaceRating() {
        compositeDisposable.add(CommunicationService.serverApi.getPlaceRating(SharedPreferencesUtils.getUserId(), placeId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { rating -> handleLoadPlaceRatingResponse(rating) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleLoadPlaceHereDataResponse(placeData: PlaceData) {
        showPlaceHereData(placeData)
        view.showProgressIndicator(false)
        view.showInfoLayout(true)
    }

    private fun handleLoadPlaceRatingResponse(rating: Int) {
        view.showRatingOnRatingBar(rating)
    }

    private fun handleErrorResponse(error: Throwable) {
        view.showProgressIndicator(false)
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

    private fun showPlaceHereData(placeData: PlaceData) {
        view.showLocation(placeData.location?.address?.text ?: "")
        view.showOpeningHours(placeData.extended?.openingHours?.text ?: "")
        view.showContacts(placeData.contacts!!)
    }
}
