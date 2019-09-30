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
import kotlinx.android.synthetic.main.fab_check.*


class SearchElementActivity : AppCompatActivity(), SearchElementContract.View {

    @Inject
    lateinit var presenter: SearchElementContract.Presenter
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var map: Map
    private val placesContainer = MapContainer()
    private lateinit var selectedMapMarker: MapMarker

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_LOCATION = "location"
        const val REQUEST_SEARCH = 1
        const val EXTRA_CATEGORY = "category"
        const val EXTRA_PLACE_ID = "placeId"
        const val EXTRA_HREF = "href"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_element)
        initializeMap()

        fabCheck.setOnClickListener {
            returnResultAndFinish(selectedMapMarker.title, selectedMapMarker.description)
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
                    searchViewCity.setQuery(city.name, false)
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

    override fun returnResultAndFinish(name: String, description: String) {
        val placeInfo = description.split('\n')
        val resultIntent = Intent().apply {
            putExtra(EXTRA_NAME, name)
            putExtra(EXTRA_LOCATION, placeInfo[0])
            putExtra(EXTRA_PLACE_ID,  placeInfo[1])
            putExtra(EXTRA_HREF,  placeInfo[2])
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun showSnackbar(messageCode: Int) {
        Snackbar.make(linearLayoutSearchElement, messageCode, Snackbar.LENGTH_LONG).show()
    }

    override fun loadObjectsOnMap(places: Array<Place>) {
        map.removeMapObjects(placesContainer.allMapObjects)
        placesContainer.removeAllMapObjects()

        for (place in places) {
            val defaultMarker = MapMarker()
            defaultMarker.title = place.title
            defaultMarker.description = (place.vicinity + "\n" + place.id + "\n" + place.href)
            defaultMarker.coordinate = GeoCoordinate(place.position[0], place.position[1], 0.0)
            placesContainer.addMapObject(defaultMarker)
            map.addMapObject(defaultMarker)
        }
    }

    private fun initializeMap() {
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        supportMapFragment.init { error ->
            if (error == OnEngineInitListener.Error.NONE) {
                supportMapFragment.mapGesture.addOnGestureListener(provideOnGestureListener())
                map = supportMapFragment.map

                val gdanskGeoCord = GeoCoordinate(54.339787, 18.609653, 0.0)
                map.setCenter(gdanskGeoCord, Map.Animation.NONE)
                val zoomRate = 0.56
                map.zoomLevel = (map.maxZoomLevel + map.minZoomLevel) * zoomRate
                loadObjectsOnMap(map.center)
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
        val category = intent.getStringExtra(EXTRA_CATEGORY)
        presenter.search(category, map.boundingBox.topLeft.longitude.toString(),
                map.boundingBox.bottomRight.latitude.toString(),
                map.boundingBox.bottomRight.longitude.toString(),
                map.boundingBox.topLeft.latitude.toString())
    }
}
