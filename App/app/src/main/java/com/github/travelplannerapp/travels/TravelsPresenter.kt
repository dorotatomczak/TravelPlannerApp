package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.CommunicationService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {


    private var travels = listOf<String>()
    private var server = CommunicationService

    override fun loadTravels() {
        val requestInterface = Retrofit.Builder()
                .baseUrl(server.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(TravelsContract.ServerAPI::class.java)

        view.loadTravels(requestInterface, this::handleResponse)
    }

    override fun onBindTravelsAtPosition(position: Int, itemView: TravelsContract.TravelItemView) {
        val travel = travels[position]
        itemView.setName(travel)
    }

    override fun getTravelsCount(): Int {
        return travels.size
    }

    override fun openTravelDetails(position: Int) {
        val travel = travels[position]
        view.showTravelDetails(travel)
    }
    override fun handleResponse(myTravels: List<String>) {

        travels = ArrayList(myTravels)

        if (travels.isEmpty()){
            view.showNoTravels()
        }else{
            view.showTravels()
        }

    }
}
