package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.communication.CommunicationService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter {

    private var server: CommunicationService = CommunicationService()
    private var travels = listOf<String>()

    override fun loadTravels() {
        //TODO [Dorota] When database is implemented load travels from it, these values are temporary
        travels = listOf("Elbląg", "Gdańsk", "Toruń", "Olsztyn")

        if (travels.isEmpty()){
            view.showNoTravels()
        }else{
            view.showTravels()
        }
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

    //TODO [Dorota] Presenter should use an Interactor, meaning it delegates the data pulling job
    // to another class and gets its results to render the view
    override fun contactServer() {
        GlobalScope.launch{
            try {
//                val response = server.getExampleData(1, view)
                val response = "200"
                view.showSnackbar(response)
            }
            catch(err: Throwable){
                println("some exception handling")
            }
        }
    }
}
