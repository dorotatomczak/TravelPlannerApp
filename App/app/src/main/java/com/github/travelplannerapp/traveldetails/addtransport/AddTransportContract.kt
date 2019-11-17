package com.github.travelplannerapp.traveldetails.addtransport

import com.github.travelplannerapp.communication.commonmodel.Routes

interface AddTransportContract {

    interface View {
        fun setFromTransport(from: String)
        fun setToTransport(to: String)
        fun showSnackbar(messageCode: Int)
        fun showTransportResult(routes: Routes)
    }

    interface Presenter {
        fun initFromToTransport()
        fun onAddTransportClicked(fromTime: String)
    }
}
