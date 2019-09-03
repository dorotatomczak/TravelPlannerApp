package com.github.travelplannerapp.dayplans.addplan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_plan.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject


//TODO [Dorota] This could be also for edit plan, if so then rename it to AddEditPlanActivity
class AddPlanActivity : AppCompatActivity(), AddPlanContract.View {

    @Inject
    lateinit var presenter: AddPlanContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        DrawerUtils.getDrawer(this, toolbar)

        // Set up edit texts
        editTextPlanFromDate.setOnClickListener { openDatePicker(it as TextInputEditText) }
        editTextPlanToDate.setOnClickListener { openDatePicker(it as TextInputEditText) }
        editTextPlanFromTime.setOnClickListener { openTimePicker(it as TextInputEditText) }
        editTextPlanToTime.setOnClickListener { openTimePicker(it as TextInputEditText) }

        editTextPlanName.setOnClickListener{
            //TODO [Dorota] Open Search activity
            //temp for testing (this should be invoked in presenter after the search returned result):
            showLocation("Champ de Mars, 5 Avenue Anatole France, 75007 Paris, Francja")
        }

        fabFinishAddPlan.setOnClickListener { onFabFinishAddPlanClicked() }
    }

    override fun showLocation(location: String) {
        inputLayoutPlanLocation.visibility = View.VISIBLE
        editTextPlanLocation.setText(location, TextView.BufferType.EDITABLE)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutAddPlan, messageCode, Snackbar.LENGTH_LONG).show()
    }

    private fun openDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val presentYear = calendar.get(Calendar.YEAR)
        val presentMonth = calendar.get(Calendar.MONTH)
        val presentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val gregorianCalendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
            val formattedDate = DateTimeUtils.dateToString(gregorianCalendar)
            editText.setText(formattedDate, TextView.BufferType.EDITABLE)
        }, presentYear, presentMonth, presentDay)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
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

    private fun onFabFinishAddPlanClicked() {
        val data = AddPlanContract.NewPlanData(
                editTextPlanName.text.toString(),
                editTextPlanFromDate.text.toString(),
                editTextPlanFromTime.text.toString(),
                editTextPlanToDate.text.toString(),
                editTextPlanToTime.text.toString()
        )
        presenter.addPlan(data)
    }
}
