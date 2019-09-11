package com.github.travelplannerapp.tickets

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.github.travelplannerapp.R

class TicketsActionModeToolbar(private val ticketsPresenter: TicketsContract.Presenter): ActionMode.Callback {

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeleteTickets ->{
                ticketsPresenter.deleteTickets()
            }
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_tickets, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        ticketsPresenter.enterActionMode()
        menu.findItem(R.id.menuDeleteTickets).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        ticketsPresenter.leaveActionMode()
    }
}
