package com.github.travelplannerapp.signup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import com.github.travelplannerapp.signin.SignInActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    @Inject
    lateinit var presenter: SignUpContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //set up buttons
        buttonSignIn.setOnClickListener { presenter.signIn() }
        buttonSignUp.setOnClickListener { presenter.signUp() }
    }

    override fun showSignIn() {
        // Goes back to previously started SignInActivity
        finish()
    }
}
