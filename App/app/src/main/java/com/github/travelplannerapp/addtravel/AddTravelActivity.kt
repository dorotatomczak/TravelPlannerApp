package com.github.travelplannerapp.addtravel

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import javax.inject.Inject

class AddTravelActivity : AppCompatActivity(), AddTravelContract.View {

    @Inject
    lateinit var presenter: AddTravelContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)
    }
}
