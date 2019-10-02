package com.github.travelplannerapp.signin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.signup.SignUpActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import com.google.android.material.snackbar.Snackbar


class SignInActivity : AppCompatActivity(), SignInContract.View {

    @Inject
    lateinit var presenter: SignInContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //set up buttons
        buttonSignIn.setOnClickListener {
            presenter.signIn(editTextEmail.text.toString(), editTextPassword.text.toString())
        }

        buttonSignUp.setOnClickListener { presenter.signUp() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SignUpActivity.REQUEST_SIGN_UP -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val messageCode = data.getIntExtra(SignUpActivity.REQUEST_SIGN_UP_RESULT,
                            R.string.try_again)
                    showSnackbar(messageCode)
                }
            }
        }
    }

    override fun showSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivityForResult(intent, SignUpActivity.REQUEST_SIGN_UP)
    }

    override fun signIn(credentials: SharedPreferencesUtils.Credentials) {
        SharedPreferencesUtils.setCredentials(credentials)

        val intent = Intent(this, TravelsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(linearLayoutSignIn, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showSnackbar(id: Int) {
        showSnackbar(resources.getString(id))
    }
}
