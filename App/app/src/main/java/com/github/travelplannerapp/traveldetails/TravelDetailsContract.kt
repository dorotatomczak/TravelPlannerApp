package com.github.travelplannerapp.traveldetails

interface TravelDetailsContract {

    interface View {
        fun setTitle(title: String)
    }

    interface TileItemView {
        fun setBackgroundColor(tileIndex: Int)
        fun setImage(tileIndex: Int)
        fun setName(tileIndex: Int)
        fun setHeight(tileIndex: Int)
    }

    interface Presenter {
        fun loadTravel()
//        fun getTilesCount(): Int
        fun onBindTileAtPosition(position: Int, itemView: TileItemView)
    }
}