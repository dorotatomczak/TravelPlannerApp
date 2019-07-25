package com.github.travelplannerapp.addtravel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class AddTravelActivity : AppCompatActivity(), AddTravelContract.View {

    @Inject
    lateinit var presenter: AddTravelContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)
    }
}
