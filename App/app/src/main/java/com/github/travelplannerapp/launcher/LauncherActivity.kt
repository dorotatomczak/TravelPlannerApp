package com.github.travelplannerapp.launcher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.travelplannerapp.R
import com.github.travelplannerapp.signin.SignInActivity
import com.github.travelplannerapp.travels.TravelsActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class LauncherActivity : AppCompatActivity(), LauncherContract.View {

    @Inject
    lateinit var presenter: LauncherContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        presenter.redirect()
    }

    override fun showSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
    }

    override fun showTravels() {
        val intent = Intent(this, TravelsActivity::class.java)
        startActivity(intent)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
    }

    override fun getCredentials(): LauncherContract.Credentials {
        val sharedPref = getSharedPreferences(resources.getString(R.string.auth_settings),
                Context.MODE_PRIVATE)
        val email = sharedPref.getString(resources.getString(R.string.email_shared_pref),
                "default").toString()
        val authToken = sharedPref.getString(resources.getString(R.string.auth_token_shared_pref),
                "default").toString()
        return LauncherContract.Credentials(email, authToken)
    }
}
