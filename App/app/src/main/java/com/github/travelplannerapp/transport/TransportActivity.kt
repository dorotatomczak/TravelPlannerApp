package com.github.travelplannerapp.transport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class TransportActivity : AppCompatActivity(), TransportContract.View {

    @Inject
    lateinit var presenter: TransportContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport)
    }
}
