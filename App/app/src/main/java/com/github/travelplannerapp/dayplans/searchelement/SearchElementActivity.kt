package com.github.travelplannerapp.dayplans.searchelement

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.dayplans.searchelement.SearchElementActivity.Companion.SEARCH_RESULT
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
    private lateinit var placesContainer: MapContainer
    private lateinit var selectedMapMarker: MapMarker

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
        const val NAME = "NAME"
        const val LOCATION = "LOCATION"
        const val REQUEST_SEARCH = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_element)
        initializeMap()

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewCity.apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
            setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    Log.e("e","sssss")
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

            })
        }
    }

    override fun returnResultAndFinish(messageCode: Int) {
        val resultIntent = Intent().apply {
            putExtra(SEARCH_RESULT, messageCode)
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
                map.setCenter(GeoCoordinate(0.0, 0.0, 0.0), Map.Animation.NONE)
                val zoomRate = 0.2
                map.zoomLevel = (map.maxZoomLevel + map.minZoomLevel) * zoomRate

                val defaultMarker = MapMarker()
                defaultMarker.title="hhhh"
                defaultMarker.coordinate = GeoCoordinate(0.0, 0.0, 0.0)
                placesContainer=MapContainer()
                placesContainer.addMapObject(defaultMarker)
                map.addMapObject(defaultMarker)
                //defaultMarker.showInfoBubble()

            } else {
                println("ERROR: Cannot initialize Map Fragment")
                Log.e("hhhh", "Cannot initialize SupportMapFragment ($error)")
            }
        }
    }

    private fun provideOnGestureListener(): MapGesture.OnGestureListener {
        return object : MapGesture.OnGestureListener.OnGestureListenerAdapter() {
            override fun onPanEnd() {
                Log.e("e", supportMapFragment.map.boundingBox.topLeft.toString())
            }
            override fun onMapObjectsSelected(objects: List<ViewObject>): Boolean {
                for (viewObj in objects) {
                    if (viewObj.baseType == ViewObject.Type.USER_OBJECT) {
                        if ((viewObj as MapObject).type == MapObject.Type.MARKER) {

                            // save the selected marker to use during route calculation
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
                return false
            }
        }
    }
}
