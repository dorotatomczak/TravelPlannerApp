package com.github.travelplannerapp.signup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    @Inject
    lateinit var presenter: SignUpContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}
