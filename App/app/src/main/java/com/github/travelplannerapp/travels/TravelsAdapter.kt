package com.github.travelplannerapp.travels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.actionmodewithdelete.DeletableElementsContract
import com.github.travelplannerapp.actionmodewithdelete.ActionModeToolbarWithDelete
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_travel.*

class TravelsAdapter(val presenter: TravelsContract.Presenter) : RecyclerView.Adapter<TravelsAdapter.TravelsViewHolder>() {

    private var actionMode: ActionMode? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelsViewHolder {
        return TravelsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_travel, parent, false)); }

    override fun getItemCount(): Int {
        return presenter.getTravelsCount()
    }

    override fun onBindViewHolder(holder: TravelsViewHolder, position: Int) {
        presenter.onBindTravelsAtPosition(position, holder)
    }

    fun leaveActionMode() {
        actionMode = null
    }

    inner class TravelsViewHolder(val presenter: TravelsContract.Presenter, override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer, TravelsContract.TravelItemView, DeletableElementsContract.ItemView,
            View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)
            checkboxItemTravel.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                    fun(_: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            presenter.setCheck(adapterPosition, true)
                        } else {
                            presenter.setCheck(adapterPosition, false)
                        }
                    }
            ))
        }

        override fun onClick(v: View?) {
            if (actionMode != null) checkboxItemTravel.isChecked = !checkboxItemTravel.isChecked
            else presenter.openTravelDetails(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            checkboxItemTravel.isChecked = true
            if (actionMode == null) actionMode = (containerView.context as AppCompatActivity)
                    .startSupportActionMode(ActionModeToolbarWithDelete(presenter))
            return true
        }

        override fun setName(name: String) {
            textViewItemTravelName.text = name
        }

        override fun setCheckbox(checked: Boolean) {
            if (actionMode != null) checkboxItemTravel.visibility = View.VISIBLE
            else checkboxItemTravel.visibility = View.GONE

            checkboxItemTravel.isChecked = checked
        }
    }
}
