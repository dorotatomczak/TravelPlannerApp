package com.github.travelplannerapp.deleteactionmode

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.github.travelplannerapp.R

class DeleteActionModeToolbar(private val presenter: DeleteContract.Presenter) : ActionMode.Callback {
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDelete -> {
                presenter.onDeleteClicked()
            }
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        presenter.enterActionMode()
        menu.findItem(R.id.menuDelete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        presenter.leaveActionMode()
    }
}
