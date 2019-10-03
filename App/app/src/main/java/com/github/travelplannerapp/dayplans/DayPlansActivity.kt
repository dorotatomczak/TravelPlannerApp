package com.github.travelplannerapp.dayplans

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.model.Plan
import com.github.travelplannerapp.dayplans.addplan.AddPlanActivity
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
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        fabAdd.setOnClickListener { presenter.onAddPlanClicked() }

        swipeRefreshLayoutDayPlans.setOnRefreshListener { refreshDayPlans() }

        recyclerViewDayPlans.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewDayPlans.adapter = DayPlansAdapter(presenter)

        refreshDayPlans()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AddPlanActivity.REQUEST_ADD_PLAN -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val messageCode = data.getIntExtra(AddPlanActivity.REQUEST_ADD_PLAN_RESULT_MESSAGE,
                            R.string.scanner_general_error)
                    showSnackbar(messageCode)
                    val plan = data.getSerializableExtra(AddPlanActivity.REQUEST_ADD_PLAN_RESULT_PLAN) as Plan
                    presenter.onPlanAdded(plan)
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

    override fun showAddPlan(travelId: Int) {
        val intent = Intent(this, AddPlanActivity::class.java)
        intent.putExtra(AddPlanActivity.EXTRA_TRAVEL_ID, travelId)
        startActivityForResult(intent, AddPlanActivity.REQUEST_ADD_PLAN)
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

    private fun refreshDayPlans() {
        swipeRefreshLayoutDayPlans.isRefreshing = true
        presenter.loadDayPlans()
    }
}
