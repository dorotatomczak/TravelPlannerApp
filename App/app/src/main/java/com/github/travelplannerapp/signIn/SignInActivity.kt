package com.github.travelplannerapp.signIn

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.R

import javax.inject.Inject

import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), SignInContract.View {

    @Inject
    lateinit var presenter: SignInContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //set up toolbar
        setSupportActionBar(toolbarSignIn)

        //set up button
        buttonSignIn.setOnClickListener { presenter.signIn() }
    }

    override fun showTravels() {
        val intent = Intent(this, TravelsActivity::class.java)
        startActivity(intent)
    }
}
