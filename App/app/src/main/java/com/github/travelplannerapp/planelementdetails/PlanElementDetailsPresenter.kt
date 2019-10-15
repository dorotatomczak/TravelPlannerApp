package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.BasePresenter
import io.reactivex.disposables.CompositeDisposable

class PlanElementDetailsPresenter(view: PlanElementDetailsContract.View) : BasePresenter<PlanElementDetailsContract.View>(view), PlanElementDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
}