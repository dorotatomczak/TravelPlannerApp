package com.github.travelplannerapp.planelementdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.utils.DrawerUtils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_travel_details.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class PlanElementDetailsActivity : AppCompatActivity(), PlanElementDetailsContract.View  {

    @Inject
    lateinit var presenter: PlanElementDetailsContract.Presenter

    companion object {
        const val EXTRA_PLACE = "place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_element_details)

        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)
        setTitle("tytul")

    }

    override fun setTitle(title: String) {
        collapsing.title = title
    }
}