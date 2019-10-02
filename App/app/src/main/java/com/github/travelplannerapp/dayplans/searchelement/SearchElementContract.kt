package com.github.travelplannerapp.dayplans.searchelement
import com.github.travelplannerapp.communication.model.Contacts
import com.github.travelplannerapp.communication.model.Place
import com.here.android.mpa.mapping.MapMarker

interface SearchElementContract {
    interface View{
        fun returnResultAndFinish()
        fun loadObjectsOnMap(places: Array<Place>)
        fun showSnackbar(messageCode: Int)
        fun showContacts(contacts: Contacts)
    }
    interface Presenter{
        fun search(category: String, west: String, north: String, east: String, south: String)
        fun clearPlacesMap()
        fun savePlaceInMap(marker: MapMarker, place: Place)
        fun getPlace(marker: MapMarker): Place
        fun setContacts(objectId: String, href: String)
    }
}