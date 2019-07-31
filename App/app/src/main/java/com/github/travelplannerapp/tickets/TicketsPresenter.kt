package com.github.travelplannerapp.tickets

import com.github.travelplannerapp.BasePresenter

class TicketsPresenter(view: TicketsContract.View) : BasePresenter<TicketsContract.View>(view), TicketsContract.Presenter {

    override fun onAddTravelClick() {
        if (view.verifyPermissions()) {
            view.openCamera()
        } else {
            view.requestPermissions()
        }
    }

}