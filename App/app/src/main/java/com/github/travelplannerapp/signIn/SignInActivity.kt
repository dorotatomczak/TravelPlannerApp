package com.github.travelplannerapp.signIn

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Button

import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.R

import javax.inject.Inject

import dagger.android.AndroidInjection

class SignInActivity : AppCompatActivity(), SignInContract.View {

    @Inject
    lateinit var presenter: SignInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //set up button
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { presenter.signIn() }
    }

    override fun showTravels() {
        val intent = Intent(this, TravelsActivity::class.java)
        startActivity(intent)
    }
}
