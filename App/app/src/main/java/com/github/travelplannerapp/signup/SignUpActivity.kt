package com.github.travelplannerapp.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ServerApi
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    @Inject
    lateinit var presenter: SignUpContract.Presenter
    var myCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //set up buttons
        buttonSignIn.setOnClickListener { presenter.signIn() }
        buttonSignUp.setOnClickListener {
            presenter.signUp(editTextEmail.text.toString(),
                    editTextPassword.text.toString(), editTextConfirmPassword.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable.clear()
    }

    override fun sendSignUpRequest(requestInterface: ServerApi, jsonLoginRequest: String,
                                   handleSignUpResponse: (jsonString: String) -> Unit) {
        myCompositeDisposable.add(requestInterface.register(jsonLoginRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(handleSignUpResponse, { showSnackbar(resources.getString(R.string.server_connection_failure), null) }))
    }

    override fun showSignIn() {
        // Goes back to previously started SignInActivity
        finish()
    }

    override fun signUp() {
        showSnackbar(resources.getString(R.string.sing_up_successful),
                View.OnClickListener {
                    showSignIn()
                })
    }

    override fun showSnackbar(message: String, listener: View.OnClickListener?) {
        Snackbar.make(linearLayoutSignUp, message, Snackbar.LENGTH_LONG)
                .setAction("OK", listener).show()
    }

    override fun showSnackbar(id: Int, listener: View.OnClickListener?) {
        showSnackbar(resources.getString(id), listener)
    }
}
