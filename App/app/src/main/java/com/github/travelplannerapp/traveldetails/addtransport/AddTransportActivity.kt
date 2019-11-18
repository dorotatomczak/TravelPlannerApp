package com.github.travelplannerapp.traveldetails.addtransport

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.commonmodel.Routes
import com.github.travelplannerapp.utils.DrawerUtils
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_transport.*
import kotlinx.android.synthetic.main.fab_search.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class AddTransportActivity : AppCompatActivity(), AddTransportContract.View {

    @Inject
    lateinit var presenter: AddTransportContract.Presenter

    companion object {
        const val EXTRA_FROM = "EXTRA_FROM"
        const val EXTRA_TO = "EXTRA_TO"
        const val EXTRA_TRAVEL_ID = "EXTRA_TRAVEL_ID"
        const val EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE"
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

        presenter.initFromToTransport()

        fabSearch.setOnClickListener { presenter.onSearchTransportClicked() }

        setOnSpinnerItemSelectListener()
    }

    override fun setFromTransport(from: String) {
        textViewFromAddTransport.text = from
    }

    override fun setToTransport(to: String) {
        textViewToAddTransport.text = to
    }

    override fun setDepartureDate(departureDate: String) {
        textViewDepartureDate.text = departureDate
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(coordinatorLayoutAddTransport, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun showTransportResult(routes: Routes) {
        var result = ""
        routes.routes.forEach { route ->
            run {
                result += "distance: ${route.legs[0].distance.value}\n" +
                        "duration: ${route.legs[0].duration.value}\n" +
                        "steps:\n"
                for (i in 0 until (route.legs[0].steps.size)) {
                    result += "${route.legs[0].steps[i].html_instructions}\n"
                }
            }
            result+="\n"
        }
        addTransportResult.text = result
    }

    private fun setOnSpinnerItemSelectListener() {
        dropdownTravelMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // pass
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val mode =
                        when (dropdownTravelMode.selectedItem.toString()) {
                            resources.getString(R.string.walking) -> "walking"
                            resources.getString(R.string.transit) -> "transit"
                            else -> "driving"
                        }
                presenter.onTravelModeSelected(mode)
            }
        }
    }
}
