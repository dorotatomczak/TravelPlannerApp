package com.github.travelplannerapp.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.initializers.AppInitializer
import com.github.travelplannerapp.signin.SignInActivity
import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import dagger.android.AndroidInjection
import javax.inject.Inject

class LauncherActivity : AppCompatActivity(), LauncherContract.View {

    @Inject
    lateinit var presenter: LauncherContract.Presenter
    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        appInitializer.initialize()
        presenter.redirect(SharedPreferencesUtils.getCredentials())
    }

    override fun showSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun showTravels() {
        val intent = Intent(this, TravelsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
