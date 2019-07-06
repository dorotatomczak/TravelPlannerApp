package com.github.travelplannerapp.accommodation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class AccommodationActivity : AppCompatActivity(), AccommodationContract.View {

    @Inject
    lateinit var presenter: AccommodationContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accommodation)
    }
}
