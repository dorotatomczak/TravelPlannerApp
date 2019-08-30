package com.github.travelplannerapp.searchelement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.github.travelplannerapp.R
import dagger.android.AndroidInjection
import javax.inject.Inject
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_search_element.*


class SearchElementActivity : AppCompatActivity(), SearchElementContract.View, AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var presenter: SearchElementContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_element)

        presenter.populateElementSpinner()
    }

    override fun populateElementSpinner(items: Array<Int>) {
        var stringItems = emptyArray<String>()

        items.forEach { stringItems += resources.getString(it) }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, stringItems)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSearchElementCategories!!.adapter = aa
        spinnerSearchElementCategories!!.onItemSelectedListener = this
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
