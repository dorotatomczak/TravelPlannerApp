package com.github.travelplannerapp.tickets

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class TicketsPresenter(view: TicketsContract.View, private val travelId: Int) : BasePresenter<TicketsContract.View>(view), TicketsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var tickets = ArrayList<String>()

    override fun onAddScanClick() {
        if (view.verifyPermissions()) {
            view.openCamera()
        } else {
            view.requestPermissions()
        }
    }

    override fun onPhotoTaken() {
        view.showScanner(travelId)
    }

    override fun loadScans(token: String, userId: Int) {
        compositeDisposable.add(CommunicationService.serverApi.getScans(token, userId, travelId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.statusCode == 200) it.data!! else throw ApiException(it.statusCode) }
                .subscribe(
                        { scans -> handleLoadScansResponse(scans) },
                        { handleErrorResponse() }
                ))
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun getTicketsCount(): Int {
        return tickets.size
    }

    override fun onBindTravelsAtPosition(position: Int, itemView: TicketsContract.TicketItemView) {
        val ticket = tickets[position]
        itemView.setImage(CommunicationService.getScanUrl(ticket))
    }

    override fun onAddedScan(name: String) {
        tickets.add(name)
        view.showTickets()
        view.onDataSetChanged()
    }

    private fun handleLoadScansResponse(scans: List<String>) {
        tickets = ArrayList(scans)
        view.onDataSetChanged()
        view.hideLoadingIndicator()

        if (this.tickets.isEmpty()) view.showNoTickets() else view.showTickets()
    }

    private fun handleErrorResponse() {
        view.showSnackbar(R.string.server_connection_error)
    }

}