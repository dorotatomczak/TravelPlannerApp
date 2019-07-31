package com.github.travelplannerapp.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.signup.SignUpActivity

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

        //set up buttons
        buttonSignIn.setOnClickListener { presenter.signIn() }
        buttonSignUp.setOnClickListener { presenter.signUp() }
    }

    override fun showTravels() {
        val intent = Intent(this, TravelsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
