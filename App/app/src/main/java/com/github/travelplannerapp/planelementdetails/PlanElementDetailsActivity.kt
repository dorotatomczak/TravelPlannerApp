package com.github.travelplannerapp.planelementdetails

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.Contacts
import com.github.travelplannerapp.utils.ActivityUtils
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
        const val EXTRA_PLAN_ELEMENT = "plan_element"
        const val EXTRA_PLACE_NAME = "place_name"
        const val EXTRA_CAN_BE_RATED = "can_be_rated"
        const val EXTRA_TRAVEL_ID = "travel_id"
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

        buttonSaveNotesPlanElementInfo.setOnClickListener {
            ActivityUtils.hideKeyboard(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager, currentFocus)

            presenter.updatePlanElement(
                    editTextNotesPlanElementInfo.text.toString()
            )
        }

        presenter.loadPlace()
        editTextNotesPlanElementInfo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // pass
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                showSaveButtonVisibility(true)
            }

            override fun afterTextChanged(s: Editable) {
                // pass
            }
        })
    }

    override fun showSaveButtonVisibility(isVisible: Boolean) {
        if (isVisible) buttonSaveNotesPlanElementInfo.visibility = View.VISIBLE
        else buttonSaveNotesPlanElementInfo.visibility = View.GONE
    }

    override fun showNotes(notes: String) {
        editTextNotesPlanElementInfo.setText(notes, TextView.BufferType.EDITABLE)
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
