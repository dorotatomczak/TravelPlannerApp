package com.github.travelplannerapp.dayplans

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class DayPlansActivity : AppCompatActivity(), DayPlansContract.View{

    @Inject
    lateinit var presenter: DayPlansContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_plans)
    }
}
