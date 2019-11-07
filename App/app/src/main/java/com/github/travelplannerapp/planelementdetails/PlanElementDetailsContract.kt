package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.communication.commonmodel.Contacts

interface PlanElementDetailsContract {

    interface View {
        fun showSnackbar(messageCode: Int)
        fun showInfoLayout(isVisible: Boolean)
        fun showProgressIndicator(isVisible: Boolean)
        fun showSaveButtonVisibility(isVisible: Boolean)

        fun showTitle(title: String)
        fun showName(name: String)
        fun showLocation(location: String)
        fun showOpeningHours(openingHours: String?)
        fun showAverageRating(rating: String)
        fun showContacts(contacts: Contacts)
        fun showNotes(notes: String)

        fun showRatingOnRatingBar(rating: Int)
        fun changeRatingTextToCompleted()
    }

    interface Presenter {
        fun loadPlace()
        fun onRatingChanged(rating: Int)
        fun updatePlanElement(newNotes: String)
    }
}
