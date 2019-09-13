package com.github.travelplannerapp.dayplans.searchelement
import com.github.travelplannerapp.communication.model.Place

interface SearchElementContract {
    interface View{
        fun returnResultAndFinish(messageCode: Int, name: String, address: String)
        fun loadObjectsOnMap(places: Array<Place>)
        fun showSnackbar(messageCode: Int)
    }
    interface Presenter{
        fun search(category: String, west: String, north: String, east: String, south: String)
    }
}