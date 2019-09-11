package com.github.travelplannerapp.dayplans

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.dayplans.addplan.AddPlanActivity
import com.github.travelplannerapp.utils.DrawerUtils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_day_plans.*
import kotlinx.android.synthetic.main.fab_add.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class DayPlansActivity : AppCompatActivity(), DayPlansContract.View {

    @Inject
    lateinit var presenter: DayPlansContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_plans)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        fabAdd.setOnClickListener {
            showAddPlan()
        }

        swipeRefreshLayoutDayPlans.setOnRefreshListener {
            swipeRefreshLayoutDayPlans.isRefreshing = false
        }

//        TODO recyclerViewDayPlans.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        TODO recyclerViewDayPlans.adapter = DayPlansAdapter(presenter)
    }

    private fun showAddPlan() {
        val intent = Intent(this, AddPlanActivity::class.java)
        startActivity(intent)
    }
}
