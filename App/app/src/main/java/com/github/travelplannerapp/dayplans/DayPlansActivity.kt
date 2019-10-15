package com.github.travelplannerapp.dayplans

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.appmodel.PlanElement
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.dayplans.addplanelement.AddPlanElementActivity
import com.github.travelplannerapp.planelementdetails.PlanElementDetailsActivity
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_day_plans.*
import kotlinx.android.synthetic.main.fab_add.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class DayPlansActivity : AppCompatActivity(), DayPlansContract.View {

    @Inject
    lateinit var presenter: DayPlansContract.Presenter

    companion object {
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_plans)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.day_plans)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        fabAdd.setOnClickListener { presenter.onAddPlanElementClicked() }

        swipeRefreshLayoutDayPlans.setOnRefreshListener { refreshDayPlans() }

        recyclerViewDayPlans.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewDayPlans.adapter = DayPlansAdapter(presenter)

        refreshDayPlans()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AddPlanElementActivity.REQUEST_ADD_PLAN -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val messageCode = data.getIntExtra(AddPlanElementActivity.REQUEST_ADD_PLAN_ELEMENT_RESULT_MESSAGE,
                            R.string.try_again)
                    showSnackbar(messageCode)
                    val plan = data.getSerializableExtra(AddPlanElementActivity.REQUEST_ADD_PLAN_ELEMENT_RESULT_PLAN) as PlanElement
                    presenter.onPlanElementAdded(plan)
                }
            }
        }
    }

    override fun showDayPlans() {
        textViewNoDayPlans.visibility = View.GONE
        recyclerViewDayPlans.visibility = View.VISIBLE
    }

    override fun showNoDayPlans() {
        textViewNoDayPlans.visibility = View.VISIBLE
        recyclerViewDayPlans.visibility = View.GONE
    }

    override fun showAddPlanElement(travelId: Int) {
        val intent = Intent(this, AddPlanElementActivity::class.java)
        intent.putExtra(AddPlanElementActivity.EXTRA_TRAVEL_ID, travelId)
        startActivityForResult(intent, AddPlanElementActivity.REQUEST_ADD_PLAN)
    }

    override fun onDataSetChanged() {
        recyclerViewDayPlans.adapter?.notifyDataSetChanged()
    }

    override fun hideLoadingIndicator() {
        swipeRefreshLayoutDayPlans.isRefreshing = false
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutDayPlans, getString(messageCode), Snackbar.LENGTH_SHORT).show()
    }

    override fun showActionMode() {
        fabAdd.visibility = View.GONE
    }

    override fun showNoActionMode() {
        fabAdd.visibility = View.VISIBLE
        (recyclerViewDayPlans.adapter as DayPlansAdapter).leaveActionMode()
    }

    override fun showConfirmationDialog() {
        val travelsText = getString(R.string.day_plans)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_entry, travelsText))
                .setMessage(getString(R.string.delete_confirmation, travelsText))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    presenter.deletePlanElements()
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                }
                .show()
    }

    private fun refreshDayPlans() {
        swipeRefreshLayoutDayPlans.isRefreshing = true
        presenter.loadDayPlans()
    }

    override fun showPlanElementDetails(place: Place) {
        val intent = Intent(this, PlanElementDetailsActivity::class.java)
        intent.putExtra(PlanElementDetailsActivity.EXTRA_PLACE, place)
        startActivity(intent)
    }
}
