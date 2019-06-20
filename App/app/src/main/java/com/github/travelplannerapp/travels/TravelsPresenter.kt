package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {
    //TODO [Dorota] When database is implemented retrieve travels from it
    val travels = listOf("Paris", "Tokyo", "New York", "Wałbrzych")

    override fun onBindTravelsAtPosition(position: Int, itemView: TravelsContract.TravelItemView) {
        val travel = travels[position]
        itemView.setName(travel)
    }

    override fun getTravelsCount(): Int {
        return travels.size
    }

    override fun openTravelDetails(position: Int) {
        val travel = travels[position]
        view.showTravelDetails(travel)
    }
}
