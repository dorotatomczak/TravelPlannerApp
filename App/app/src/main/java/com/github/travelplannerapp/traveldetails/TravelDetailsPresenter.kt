package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TravelDetailsPresenter(private var travel: Travel, view: TravelDetailsContract.View) :
        BasePresenter<TravelDetailsContract.View>(view), TravelDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun loadTravel() {
        view.setTitle(travel.name)
        travel.imageUrl?.let { view.showImage(CommunicationService.getTravelImageUrl(it, SharedPreferencesUtils.getUserId())) }
    }

    override fun openCategory(category: Category.CategoryType) {
        when (category) {
            Category.CategoryType.DAY_PLANS -> view.showDayPlans(travel.id)
            Category.CategoryType.TRANSPORT -> view.showTransport()
            Category.CategoryType.ACCOMMODATION -> view.showAccommodation()
            Category.CategoryType.SCANS -> view.showScans(travel.id)
        }
    }

    override fun changeTravelName(travelName: String) {
        compositeDisposable.add(CommunicationService.serverApi.changeTravelName(
                SharedPreferencesUtils.getUserId(), Travel(travel.id, travelName))
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { travel -> handleChangeTravelNameResponse(travel) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun uploadTravelImage(image: File) {
        val fileReqBody = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", image.name, fileReqBody)
        val travelIdReqBody = travel.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        compositeDisposable.add(CommunicationService.serverApi.uploadTravelImage(
                SharedPreferencesUtils.getUserId(),
                travelIdReqBody,
                filePart)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { imageUrl -> handleUploadTravelImageResponse(imageUrl) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    private fun handleChangeTravelNameResponse(travel: Travel) {
        this.travel = travel
        view.setTitle(travel.name)
        view.setResult(travel)
        view.showSnackbar(R.string.change_travel_name_ok)
    }

    private fun handleUploadTravelImageResponse(travel: Travel) {
        this.travel = travel
        view.showImage(CommunicationService.getTravelImageUrl(travel.imageUrl!!, SharedPreferencesUtils.getUserId()))
    }

    private fun handleErrorResponse(error: Throwable) {
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }
}
