package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.commonmodel.Place
import io.reactivex.disposables.CompositeDisposable

class PlanElementDetailsPresenter(view: PlanElementDetailsContract.View) : BasePresenter<PlanElementDetailsContract.View>(view), PlanElementDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun showPlaceInfo(place: Place) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveRating(stars: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}