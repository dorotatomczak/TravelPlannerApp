package com.github.travelplannerapp.dayplans.addplan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.model.PlaceCategory
import com.github.travelplannerapp.communication.model.Plan
import com.github.travelplannerapp.dayplans.searchelement.SearchElementActivity
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_plan.*
import kotlinx.android.synthetic.main.fab_check.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject


//TODO [Dorota] This could be also for edit plan, if so then rename it to AddEditPlanActivity
class AddPlanActivity : AppCompatActivity(), AddPlanContract.View {

    @Inject
    lateinit var presenter: AddPlanContract.Presenter

    private var coordinates = AddPlanContract.Coordinates(0.0, 0.0)

    companion object {
        const val REQUEST_ADD_PLAN = 0
        const val REQUEST_ADD_PLAN_RESULT_MESSAGE = "REQUEST_ADD_PLAN_RESULT_MESSAGE"
        const val REQUEST_ADD_PLAN_RESULT_PLAN = "REQUEST_ADD_PLAN_RESULT_PLAN"
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
    }

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

        editTextPlanName.setOnClickListener { startSearchElementActivity() }

        fabCheck.setOnClickListener { onFabFinishAddPlanClicked() }
    }

    override fun showLocation(location: String) {
        inputLayoutPlanLocation.visibility = View.VISIBLE
        editTextPlanLocation.setText(location, TextView.BufferType.EDITABLE)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutAddPlan, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun returnResultAndFinish(messageCode: Int, plan: Plan) {
        val resultIntent = Intent().apply {
            putExtra(REQUEST_ADD_PLAN_RESULT_MESSAGE, messageCode)
            putExtra(REQUEST_ADD_PLAN_RESULT_PLAN, plan)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun startSearchElementActivity() {
        val intent = Intent(this, SearchElementActivity::class.java)
        var categoryName: String? = null
        val categoryNameDropDown = dropdownCategoriesAddPlan.selectedItem.toString()

        for (category in PlaceCategory.values()) {
            if (this.resources.getString(category.stringResurceId) == categoryNameDropDown)
                categoryName = category.categoryName
        }

        intent.putExtra(SearchElementActivity.EXTRA_CATEGORY, categoryName)
        startActivityForResult(intent, SearchElementActivity.REQUEST_SEARCH)
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
                PlaceCategory.values()[dropdownCategoriesAddPlan.selectedItemPosition],
                editTextPlanName.text.toString(),
                editTextPlanFromDate.text.toString(),
                editTextPlanFromTime.text.toString(),
                editTextPlanToDate.text.toString(),
                editTextPlanToTime.text.toString(),
                coordinates,
                editTextPlanLocation.text.toString()
        )
        presenter.addPlan(data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SearchElementActivity.REQUEST_SEARCH -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val name = data.getStringExtra(SearchElementActivity.EXTRA_NAME)
                    val location = data.getStringExtra(SearchElementActivity.EXTRA_LOCATION)
                    val placeHereId = data.getStringExtra(SearchElementActivity.EXTRA_PLACE_HERE_ID)
                    val href = data.getStringExtra(SearchElementActivity.EXTRA_HREF)

                    presenter.onPlaceFound(placeHereId!!, href!!)
                    editTextPlanName.setText(name, TextView.BufferType.EDITABLE)
                    location?.let { showLocation(it) }
                }
            }
        }
    }

}
