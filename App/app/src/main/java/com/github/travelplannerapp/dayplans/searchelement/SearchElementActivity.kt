package com.github.travelplannerapp.dayplans.searchelement

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.model.CityObject
import com.github.travelplannerapp.communication.model.Place
import dagger.android.AndroidInjection
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.common.ViewObject
import com.here.android.mpa.mapping.*
import com.here.android.mpa.mapping.Map
import kotlinx.android.synthetic.main.activity_search_element.*


class SearchElementActivity : AppCompatActivity(), SearchElementContract.View {

    @Inject
    lateinit var presenter: SearchElementContract.Presenter
    private lateinit var map: Map
    private lateinit var supportMapFragment: SupportMapFragment
    private val placesContainer = MapContainer()
    private lateinit var selectedMapMarker: MapMarker

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
        const val NAME = "NAME"
        const val LOCATION = "LOCATION"
        const val REQUEST_SEARCH = 1
        const val OK = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_element)
        initializeMap()

        fabSelectElement.setOnClickListener {
            returnResultAndFinish(OK, selectedMapMarker.title, selectedMapMarker.description)
        }

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewCity.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)

            setOnSuggestionListener(object : androidx.appcompat.widget.SearchView.OnSuggestionListener {
                override fun onSuggestionClick(position: Int): Boolean {
                    closeKeyboard()
                    val city = CityObject(suggestionsAdapter.getItem(position) as Cursor)
                    val geoCord = GeoCoordinate(city.x.toDouble(), city.y.toDouble(), 0.0)
                    loadObjectsOnMap(geoCord)

                    return true
                }

                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

            })
        }
    }

    override fun returnResultAndFinish(messageCode: Int, name: String, address: String) {
        val resultIntent = Intent().apply {
            putExtra(SEARCH_RESULT, messageCode)
            putExtra(NAME, name)
            putExtra(LOCATION, address)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(linearLayoutSearchElement, messageCode, Snackbar.LENGTH_LONG).show()
    }

    private fun initializeMap() {
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        supportMapFragment.init { error ->
            if (error == OnEngineInitListener.Error.NONE) {
                supportMapFragment.mapGesture.addOnGestureListener(provideOnGestureListener())
                map = supportMapFragment.map

                // default center in Gdansk because why not
                val gdanskGeoCord = GeoCoordinate(54.339787, 18.609653, 0.0)
                val zoomRate = 0.3
                map.zoomLevel = (map.maxZoomLevel + map.minZoomLevel) * zoomRate

                loadObjectsOnMap(gdanskGeoCord)

            } else {
                showSnackbar(R.string.load_map_error)
            }
        }
    }

    private fun provideOnGestureListener(): MapGesture.OnGestureListener {
        return object : MapGesture.OnGestureListener.OnGestureListenerAdapter() {
            override fun onPanEnd() {
                loadObjectsOnMap(map.center)
            }

            override fun onMapObjectsSelected(objects: List<ViewObject>): Boolean {
                for (viewObj in objects) {
                    if (viewObj.baseType == ViewObject.Type.USER_OBJECT) {
                        if ((viewObj as MapObject).type == MapObject.Type.MARKER) {

                            // save to remember chosen marker
                            selectedMapMarker = viewObj as MapMarker
                            for (mapObject in placesContainer.allMapObjects) {
                                if (viewObj.baseType == ViewObject.Type.USER_OBJECT) {
                                    if ((viewObj as MapObject).type == MapObject.Type.MARKER) {
                                        viewObj.hideInfoBubble()
                                    }
                                }
                            }
                            selectedMapMarker.showInfoBubble()
                            break
                        }
                    }
                }
                return true
            }
        }
    }

    private fun closeKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
    }


    private fun loadObjectsOnMap(geoCord: GeoCoordinate) {
        map.setCenter(geoCord, Map.Animation.NONE)
        val category = intent.getStringExtra("category")
        presenter.search(category, map.boundingBox.topLeft.longitude.toString(),
                map.boundingBox.bottomRight.latitude.toString(),
                map.boundingBox.bottomRight.longitude.toString(),
                map.boundingBox.topLeft.latitude.toString())
    }

    override fun loadObjectsOnMap(places: Array<Place>) {
        map.removeMapObjects(placesContainer.allMapObjects)
        placesContainer.removeAllMapObjects()

        for (place in places) {
            val defaultMarker = MapMarker()
            defaultMarker.title = place.title
            defaultMarker.description = place.vicinity
            defaultMarker.coordinate = GeoCoordinate(place.position[0], place.position[1], 0.0)
            placesContainer.addMapObject(defaultMarker)
            map.addMapObject(defaultMarker)
        }
    }
}
