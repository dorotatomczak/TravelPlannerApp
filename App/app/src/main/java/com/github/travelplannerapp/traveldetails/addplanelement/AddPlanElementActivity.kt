package com.github.travelplannerapp.traveldetails.addplanelement

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.appmodel.PlaceCategory
import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.traveldetails.searchelement.SearchElementActivity
import com.github.travelplannerapp.utils.DateTimeUtils
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_plan_element.*
import kotlinx.android.synthetic.main.fab_check.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject


//TODO [Dorota] This could be also for edit planElement, if so then rename it to AddEditPlanActivity
class AddPlanElementActivity : AppCompatActivity(), AddPlanElementContract.View {

    @Inject
    lateinit var presenter: AddPlanElementContract.Presenter

    private var coordinates = AddPlanElementContract.Coordinates()

    companion object {
        const val REQUEST_ADD_PLAN_ELEMENT_RESULT_MESSAGE = "REQUEST_ADD_PLAN_ELEMENT_RESULT_MESSAGE"
        const val REQUEST_ADD_PLAN_ELEMENT_RESULT_PLAN = "REQUEST_ADD_PLAN_ELEMENT_RESULT_PLAN"
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
        const val REQUEST_SEARCH = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan_element)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        val travelId = intent.getIntExtra(EXTRA_TRAVEL_ID, -1)
        DrawerUtils.getDrawer(this, toolbar, travelId)

        // Set up edit texts
        editTextPlanFromDate.setOnClickListener { openDatePicker(it as TextInputEditText) }
        editTextPlanFromTime.setOnClickListener { openTimePicker(it as TextInputEditText) }
        editTextPlanToDate.setOnClickListener { openDatePicker(it as TextInputEditText) }
        editTextPlanToTime.setOnClickListener { openTimePicker(it as TextInputEditText) }

        editTextPlanName.setOnClickListener { startSearchElementActivity() }

        fabCheck.setOnClickListener { onFabFinishAddPlanClicked() }
        setOnSpinnerItemSelectListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SEARCH -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val place = data.getSerializableExtra(SearchElementActivity.EXTRA_PLACE) as Place
                    place.categoryIcon = PlaceCategory.values()[dropdownCategoriesAddPlan.selectedItemPosition].ordinal
                    presenter.onPlaceFound(place)
                    editTextPlanName.setText(place.title, TextView.BufferType.EDITABLE)
                    showLocation(place.vicinity)
                }
            }
        }
    }

    override fun showLocation(location: String) {
        inputLayoutPlanLocation.visibility = View.VISIBLE
        editTextPlanLocation.setText(location, TextView.BufferType.EDITABLE)
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutAddPlan, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun returnResultAndFinish(messageCode: Int, planElement: PlanElement) {
        val resultIntent = Intent().apply {
            putExtra(REQUEST_ADD_PLAN_ELEMENT_RESULT_MESSAGE, messageCode)
            putExtra(REQUEST_ADD_PLAN_ELEMENT_RESULT_PLAN, planElement)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun setOnSpinnerItemSelectListener() {
        dropdownCategoriesAddPlan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // pass
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isCategoryAccommodation()) {
                    linearLayoutToTime.visibility = View.VISIBLE
                } else {
                    linearLayoutToTime.visibility = View.GONE
                }
            }
        }
    }

    private fun isCategoryAccommodation(): Boolean {
        val categoryNameDropDown = dropdownCategoriesAddPlan.selectedItem.toString()

        return this.resources.getString(R.string.accommodation) == categoryNameDropDown
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
        startActivityForResult(intent, REQUEST_SEARCH)
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
        var accommodationData: AddPlanElementContract.AccommodationData? = null

        if (isCategoryAccommodation()) {
            accommodationData = AddPlanElementContract.AccommodationData(
                    editTextPlanToDate.text.toString(),
                    editTextPlanToTime.text.toString())
        }

        val placeData = AddPlanElementContract.NewPlanElementData(
                editTextPlanName.text.toString(),
                editTextPlanFromDate.text.toString(),
                editTextPlanFromTime.text.toString(),
                coordinates,
                editTextPlanLocation.text.toString(),
                accommodationData,
                editTextNotes.text.toString()
        )
        presenter.addPlanElement(placeData)
    }
}
