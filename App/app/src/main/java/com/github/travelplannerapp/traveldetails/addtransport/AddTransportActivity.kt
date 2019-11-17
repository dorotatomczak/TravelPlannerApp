package com.github.travelplannerapp.traveldetails.addtransport

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.Routes
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_transport.*
import kotlinx.android.synthetic.main.fab_check.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject

class AddTransportActivity : AppCompatActivity(), AddTransportContract.View {

    @Inject
    lateinit var presenter: AddTransportContract.Presenter

    companion object {
        const val EXTRA_FROM = "EXTRA_FROM"
        const val EXTRA_TO = "EXTRA_TO"
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transport)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        val travelId = intent.getIntExtra(EXTRA_TRAVEL_ID, -1)
        DrawerUtils.getDrawer(this, toolbar, travelId)

        // Set up edit texts
        editTextFromTimeAddTransport.setOnClickListener { openTimePicker(it as TextInputEditText) }

        presenter.initFromToTransport()

        fabCheck.setOnClickListener {
            if(editTextFromTimeAddTransport.text!!.isNotEmpty()) {
                presenter.onAddTransportClicked(editTextFromTimeAddTransport.text.toString())
            }
        }
    }

    override fun setFromTransport(from: String) {
        editTextFromAddTransport.setText(from)
    }

    override fun setToTransport(to: String) {
        editTextToAddTransport.setText(to)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutAddTransport, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun showTransportResult(routes: Routes){
        addTransportResult.text = routes.toString()
    }

    private fun openTimePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val presentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val presentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val gregorianCalendar = GregorianCalendar(0, 0, 0, hour, minute)
            val formattedTime = DateTimeUtils.timeToString(gregorianCalendar)
            editText.setText(formattedTime, TextView.BufferType.EDITABLE)
        }, presentHour, presentMinute, true).show()
    }
}
