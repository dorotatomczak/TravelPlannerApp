package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.communication.model.Travel
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class TravelDetailsPresenter(private val travelId: Int, private var travelName: String, view: TravelDetailsContract.View) :
        BasePresenter<TravelDetailsContract.View>(view), TravelDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun loadTravel() {
        view.setTitle(travelName)
    }

    override fun openCategory(category: Category.CategoryType) {
        when (category) {
            Category.CategoryType.DAY_PLANS -> view.showDayPlans()
            Category.CategoryType.TRANSPORT -> view.showTransport()
            Category.CategoryType.ACCOMMODATION -> view.showAccommodation()
            Category.CategoryType.TICKETS -> view.showTickets(travelId)
        }
    }

    override fun changeTravelName(travelName: String) {
        compositeDisposable.add(CommunicationService.serverApi.changeTravelName(Travel(travelId, travelName))
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { travel -> handleChangeTravelNameResponse(travel) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleChangeTravelNameResponse(travel: Travel) {
        travelName = travel.name
        view.setTitle(travelName)
        view.setResult(travelId, travelName)
        view.showSnackbar(R.string.change_travel_name_ok)
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
