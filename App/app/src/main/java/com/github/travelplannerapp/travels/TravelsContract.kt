package com.github.travelplannerapp.travels

import io.reactivex.Observable
import retrofit2.http.GET


//TODO [Dorota] Display message when list of travels is empty
interface TravelsContract {
    interface View {
        fun showAddTravel()
        //TODO [Dorota] Change to int (travel id) after database is implemented
        fun showTravelDetails(travel: String)

        fun showTravels()

        fun showNoTravels()

        fun showSnackbar(message: String)

        fun loadTravels(requestInterface: ServerAPI, handleResponse: (myTravels: List<String>) -> Unit)
    }

    interface TravelItemView {
        fun setName(name: String)
    }

    interface Presenter {

        fun loadTravels()

        fun getTravelsCount(): Int

        fun onBindTravelsAtPosition(position: Int, itemView: TravelItemView)

        fun openTravelDetails(position: Int)

        fun handleResponse(myTravels: List<String>)

    }

    interface ServerAPI{
        @GET("/travels")
        fun getTravels(): Observable<List<String>>

    }
}
