package com.github.travelplannerapp.planelementdetails

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.Contacts
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_plan_element_details.*
import kotlinx.android.synthetic.main.item_place_element_info.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class PlanElementDetailsActivity : AppCompatActivity(), PlanElementDetailsContract.View {

    @Inject
    lateinit var presenter: PlanElementDetailsContract.Presenter

    companion object {
        const val EXTRA_PLACE_HREF = "place"
        const val EXTRA_PLACE_ID = "place_id"
        const val EXTRA_PLACE_NAME = "place_name"
        const val EXTRA_AVERAGE_RATING = "average_rating"
        const val EXTRA_CAN_BE_RATED = "can_be_rated"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_element_details)

        showInfoLayout(false)
        showProgressIndicator(true)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        presenter.loadPlace()
    }

    override fun showTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun showInfoLayout(isVisible: Boolean) {
        if (isVisible) {
            linearLayoutPlaceElementInfo.visibility = View.VISIBLE

            if (intent.getBooleanExtra(EXTRA_CAN_BE_RATED, true)) {
                linearLayoutRatingPlanElementDetails.visibility = View.VISIBLE
            }
        } else {
            linearLayoutPlaceElementInfo.visibility = View.GONE
            linearLayoutRatingPlanElementDetails.visibility = View.GONE
        }
    }

    override fun showProgressIndicator(isVisible: Boolean) {
        if (isVisible) progressBarPlanElementDetails.visibility = View.VISIBLE
        else progressBarPlanElementDetails.visibility = View.GONE
    }

    override fun showName(name: String) {
        textViewNamePlanElementInfo.text = name
    }

    override fun showLocation(location: String) {
        textViewLocationPlanElementInfo.text = location
    }

    override fun showOpeningHours(openingHours: String?) {
        if (openingHours != null && openingHours != "") textViewOpeningHoursPlanElementInfo.text = openingHours
        else linearLayoutOpeningHours.visibility = View.GONE
    }

    override fun showAverageRating(rating: String) {
        textViewRatingPlanElementInfo.text = rating
    }

    override fun showContacts(contacts: Contacts) {
        if (!contacts.phone.isNullOrEmpty()) {
            textViewPhonePlanElementInfo.text = contacts.phone[0].value
        } else {
            linearLayoutPhone.visibility = View.GONE
        }
        if (!contacts.website.isNullOrEmpty()) {
            textViewWebsitePlanElementInfo.text = contacts.website[0].value
        } else {
            linearLayoutWebsite.visibility = View.GONE
        }
    }

    override fun showRatingOnRatingBar(rating: Int) {
        if (rating != 0) {
            changeRatingTextToCompleted()
            ratingBarPlanElementDetails.setIsIndicator(true)
            ratingBarPlanElementDetails.rating = rating.toFloat()
        } else {
            ratingBarPlanElementDetails.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, newRating, _ ->
                ratingBarPlanElementDetails.setIsIndicator(true)
                setResult(Activity.RESULT_OK)
                presenter.onRatingChanged(newRating.toInt())
            }
        }
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(nestedScrollViewPlanElementDetails, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun changeRatingTextToCompleted() {
        textViewRatePlace.text = getString(R.string.rate_place_completed)
    }
}
