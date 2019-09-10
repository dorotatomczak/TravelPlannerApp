package com.github.travelplannerapp.travels

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_travel.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.CompoundButton

class TravelsAdapter (val presenter: TravelsContract.Presenter): RecyclerView.Adapter<TravelsAdapter.TravelsViewHolder>(){
    private var mActionMode: ActionMode? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelsViewHolder {
        return TravelsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_travel, parent, false));    }

    override fun getItemCount(): Int {
        return presenter.getTravelsCount()
    }

    override fun onBindViewHolder(holder: TravelsViewHolder, position: Int) {
        presenter.onBindTravelsAtPosition(position, holder)
    }

    inner class TravelsViewHolder(val presenter: TravelsContract.Presenter, override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer, TravelsContract.TravelItemView, View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)
            checkboxItemTravel.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                fun ( _ : CompoundButton, isChecked: Boolean) {
                    if (isChecked) {
                        presenter.addPositionToDelete(adapterPosition)
                    } else {
                        presenter.removePositionToDelete(adapterPosition)
                    }
                }
            ))
        }

        override fun onClick(v: View?) {
            if (mActionMode != null) checkboxItemTravel.isChecked = !checkboxItemTravel.isChecked
            else presenter.openTravelDetails(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            mActionMode = (containerView.context as AppCompatActivity)
                    .startSupportActionMode(TravelsActionModeToolbar(presenter, this))
            // TODO [Magda] check travel when opening action mode current error: true first then notifydatachange sets false
            // checkboxItemTravel.isChecked = true
            return true
        }

        override fun setName(name: String) {
            textViewItemTravelName.text = name
        }

        override fun setCheckbox() {
            if(mActionMode != null) checkboxItemTravel.visibility = View.VISIBLE
            else checkboxItemTravel.visibility = View.GONE

            checkboxItemTravel.isChecked = false
        }

        override fun setActionModeToNull() {
            mActionMode = null
        }
    }
}
