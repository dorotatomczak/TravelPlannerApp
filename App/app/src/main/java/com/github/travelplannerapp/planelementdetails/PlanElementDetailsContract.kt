package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.communication.commonmodel.Contacts

interface PlanElementDetailsContract {

    interface View {
        fun showSnackbar(messageCode: Int)
        fun showInfoLayout(isVisible: Boolean)
        fun showProgressIndicator(isVisible: Boolean)

        fun showTitle(title: String)
        fun showName(name: String)
        fun showLocation(location: String)
        fun showOpeningHours(openingHours: String?)
        fun showAverageRating(rating: String)
        fun showContacts(contacts: Contacts)

        fun showRatingOnRatingBar(rating: Int)
        fun changeRatingTextToCompleted()
    }

    interface Presenter {
        fun showPlaceInfo(placeHref: String)
        fun saveRating(stars: Int)
        fun setAverageRating(rating: String)
        fun isRatingChanged():Boolean
    }
}
