package com.github.travelplannerapp.travels

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.github.travelplannerapp.R

class TravelsActionModeToolbar(private val travelsPresenter: TravelsContract.Presenter): ActionMode.Callback {
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeleteTravels ->{
                travelsPresenter.onDeleteClicked()
            }
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_travels, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        travelsPresenter.enterActionMode()
        menu.findItem(R.id.menuDeleteTravels).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        travelsPresenter.leaveActionMode()
    }
}
