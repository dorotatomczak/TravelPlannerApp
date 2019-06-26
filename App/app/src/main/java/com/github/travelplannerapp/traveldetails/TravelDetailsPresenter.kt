package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.BasePresenter

class TravelDetailsPresenter (private var travelId: String, view: TravelDetailsContract.View): BasePresenter<TravelDetailsContract.View>(view), TravelDetailsContract.Presenter {

    override fun loadTravel() {
        view.setTitle(travelId)
    }

    override fun onBindTileAtPosition(position: Int, itemView: TravelDetailsContract.TileItemView) {
        itemView.setBackgroundColor(position)
        itemView.setImage(position)
        itemView.setName(position)
        itemView.setHeight(position)
    }
}