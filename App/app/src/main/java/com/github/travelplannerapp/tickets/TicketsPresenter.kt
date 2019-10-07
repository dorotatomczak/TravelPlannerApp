package com.github.travelplannerapp.tickets

import com.github.travelplannerapp.BasePresenter
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.appmodel.Scan
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SchedulerProvider
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable

class TicketsPresenter(view: TicketsContract.View, private val travelId: Int) : BasePresenter<TicketsContract.View>(view), TicketsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var tickets = ArrayList<Scan>()
    private var ticketsToDelete = mutableSetOf<Scan>()

    override fun onAddScanClicked() {
        if (view.verifyPermissions()) {
            view.openCamera()
        } else {
            view.requestPermissions()
        }
    }

    override fun onPhotoTaken() {
        view.showScanner(travelId)
    }

    override fun loadScans() {
        view.setLoadingIndicatorVisibility(true)

        compositeDisposable.add(CommunicationService.serverApi.getScans(SharedPreferencesUtils.getUserId(), travelId)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { scans -> handleLoadScansResponse(scans) },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun getTicketsCount(): Int {
        return tickets.size
    }

    override fun onBindTicketsAtPosition(position: Int, itemView: TicketsContract.TicketItemView) {
        val ticket = tickets[position]
        itemView.setImage(CommunicationService.getScanUrl(ticket.name, SharedPreferencesUtils.getUserId()))
        itemView.setCheckbox()
    }

    override fun onScanAdded(scan: Scan) {
        tickets.add(scan)
        view.showTickets()
        view.onDataSetChanged()
    }

    override fun onScanClicked(position: Int) {
        val ticket = tickets[position]
        view.showFullScan((CommunicationService.getScanUrl(ticket.name, SharedPreferencesUtils.getUserId())))
    }

    override fun onDeleteClicked() {
        if (ticketsToDelete.size > 0) {
            view.showConfirmationDialog()
        }
    }

    override fun deleteTickets() {
        compositeDisposable.add(CommunicationService.serverApi.deleteScans(SharedPreferencesUtils.getUserId(), ticketsToDelete)
                .observeOn(SchedulerProvider.ui())
                .subscribeOn(SchedulerProvider.io())
                .map { if (it.responseCode == ResponseCode.OK) it.data!! else throw ApiException(it.responseCode) }
                .subscribe(
                        { handleDeleteTicketsResponse() },
                        { error -> handleErrorResponse(error) }
                ))
    }

    override fun enterActionMode() {
        view.onDataSetChanged()
        view.showActionMode()
    }

    override fun leaveActionMode() {
        view.onDataSetChanged()
        view.showNoActionMode()
    }

    override fun addTicketToDelete(position: Int) {
        ticketsToDelete.add(tickets[position])
    }

    override fun removeTicketToDelete(position: Int) {
        ticketsToDelete.remove(tickets[position])
    }

    private fun handleLoadScansResponse(scans: List<Scan>) {
        tickets = ArrayList(scans)
        view.onDataSetChanged()
        view.setLoadingIndicatorVisibility(false)

        if (this.tickets.isEmpty()) view.showNoTickets() else view.showTickets()
    }

    private fun handleDeleteTicketsResponse() {
        ticketsToDelete.clear()
        loadScans()
        view.showSnackbar(R.string.delete_tickets_ok)
    }

    private fun handleErrorResponse(error: Throwable) {
        ticketsToDelete.clear()
        loadScans()
        if (error is ApiException) view.showSnackbar(error.getErrorMessageCode())
        else view.showSnackbar(R.string.server_connection_error)
    }

}
