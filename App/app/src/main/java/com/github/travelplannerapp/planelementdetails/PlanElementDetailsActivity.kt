package com.github.travelplannerapp.planelementdetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.Contacts
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_plan_element_details.*
import kotlinx.android.synthetic.main.activity_search_element.*
import kotlinx.android.synthetic.main.activity_travel_details.collapsing
import kotlinx.android.synthetic.main.item_place_element_info.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class PlanElementDetailsActivity : AppCompatActivity(), PlanElementDetailsContract.View {

    @Inject
    lateinit var presenter: PlanElementDetailsContract.Presenter

    companion object {
        const val EXTRA_PLACE_HREF = "place"
        const val EXTRA_PLACE_NAME = "place_name"
        const val EXTRA_AVERAGE_RATING = "average_rating"
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

        progressBarPlanElementDetails.visibility = View.VISIBLE
        showTitle(intent.getStringExtra(EXTRA_PLACE_NAME)!!)
        val placeHref = intent.getStringExtra(EXTRA_PLACE_HREF)
        presenter.setAverageRating(intent.getStringExtra(EXTRA_AVERAGE_RATING) ?: "0.0")
        placeHref.let{presenter.showPlaceInfo(placeHref!!)}

    }

    override fun showTitle(title: String) {
        collapsing.title = title
    }

    override fun showInfoLayout(isVisible: Boolean){
        if (isVisible) linearLayoutPlaceElementInfo.visibility = View.VISIBLE
        else linearLayoutPlaceElementInfo.visibility = View.GONE
    }

    override fun showProgressIndicator(isVisible: Boolean){
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

    override fun showRatingOnRatingBar(rating: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(linearLayoutSearchElement, messageCode, Snackbar.LENGTH_LONG).show()
    }
}