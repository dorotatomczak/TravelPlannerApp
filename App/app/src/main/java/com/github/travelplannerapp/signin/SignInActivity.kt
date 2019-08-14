package com.github.travelplannerapp.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.signup.SignUpActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_in.*
import android.content.Context
import com.github.travelplannerapp.communication.ServerApi
import com.google.android.material.snackbar.Snackbar


class SignInActivity : AppCompatActivity(), SignInContract.View {

    @Inject
    lateinit var presenter: SignInContract.Presenter
    var myCompositeDisposable = CompositeDisposable()

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

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable.clear()
    }

    override fun authorize(requestInterface: ServerApi, jsonLoginRequest: String,
                           handleLoginResponse: (jsonString: String) -> Unit) {
        myCompositeDisposable.add(requestInterface.authenticate(jsonLoginRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(handleLoginResponse, { showSnackbar(resources.getString(R.string.server_connection_failure)) }))
    }

    override fun showSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    override fun signIn(authToken: String, email: String) {
        val sharedPref = getSharedPreferences(resources.getString(R.string.auth_settings),
                Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(resources.getString(R.string.auth_token_shared_pref), authToken)
        editor.putString(resources.getString(R.string.email_shared_pref), email)
        editor.apply()

        val intent = Intent(this, TravelsActivity::class.java)
        startActivity(intent)
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(linearLayoutSignIn, message, Snackbar.LENGTH_LONG)
                .setAction("OK", null).show()
    }

    override fun showSnackbar(id: Int) {
        showSnackbar(resources.getString(id))
    }
}
