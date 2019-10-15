package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.communication.appmodel.Travel
import java.io.File

interface TravelDetailsContract {

    interface View {
        fun setTitle(title: String)
        fun setResult(travel: Travel)
        fun showDayPlans(travelId: Int)
        fun showTransport()
        fun showAccommodation()
        fun showScans(travelId: Int)
        fun showSnackbar(messageCode: Int)
        fun showImage(url: String)
    }

    interface TileItemView {
        fun setBackgroundColor(tileIndex: Int)
        fun setImage(tileIndex: Int)
        fun setName(tileIndex: Int)
        fun setMinHeight(tileIndex: Int)
    }

    interface Presenter {
        fun loadTravel()
        fun openCategory(category: Category.CategoryType)
        fun changeTravelName(travelName: String)
        fun uploadTravelImage(image: File)
    }
}
