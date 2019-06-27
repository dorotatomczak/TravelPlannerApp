package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private var travels = listOf<String>()

    override fun loadTravels() {
        //TODO [Dorota] When database is implemented load travels from it, these values are temporary
        travels = listOf("Elbląg", "Gdańsk", "Toruń", "Olsztyn")

        if (travels.isEmpty()){
            view.showNoTravels()
        }else{
            view.showTravels()
        }
    }

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
