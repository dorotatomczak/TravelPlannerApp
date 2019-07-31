package com.github.travelplannerapp.tickets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.scanner.ScannerActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_tickets.*

import javax.inject.Inject

class TicketsActivity : AppCompatActivity(), TicketsContract.View,  NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var presenter: TicketsContract.Presenter

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        setSupportActionBar(toolbarTickets)
        supportActionBar?.setHomeButtonEnabled(true)

        toggle = ActionBarDrawerToggle(this, drawerLayoutTickets, toolbarTickets, R.string.drawer_open, R.string.drawer_close)
        drawerLayoutTickets.addDrawerListener(toggle)
        toggle.syncState()

        navigationViewTickets.setNavigationItemSelectedListener(this)

        fabTickets.setOnClickListener {
            showScanner()
        }
    }

    private fun showScanner(){
        val intent = Intent(this, ScannerActivity::class.java)
        startActivityForResult(intent, ScannerActivity.REQUEST_SCANNER)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //TODO [Dorota] Same as in travels, move to common file
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ScannerActivity.REQUEST_SCANNER -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = data?.getStringExtra(ScannerActivity.REQUEST_SCANNER_RESULT)
                    if (result != null) showSnackbar(result)
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayoutTickets, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
    }
}
