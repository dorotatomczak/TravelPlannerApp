package com.github.travelplannerapp.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_up.*
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    @Inject
    lateinit var presenter: SignUpContract.Presenter

    companion object {
        const val REQUEST_SIGN_UP_RESULT = "REQUEST_SIGN_UP_RESULT"
        const val REQUEST_SIGN_UP = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //set up buttons
        buttonSignIn.setOnClickListener { presenter.onSignInClicked() }
        buttonSignUp.setOnClickListener {
            presenter.onSignUpClicked(editTextEmail.text.toString(),
                    editTextPassword.text.toString(), editTextConfirmPassword.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun showSignIn() {
        // Goes back to previously started SignInActivity
        finish()
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(linearLayoutSignUp, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun returnResultAndFinish(messageCode: Int) {
        val resultIntent = Intent().apply {
            putExtra(REQUEST_SIGN_UP_RESULT, messageCode)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
