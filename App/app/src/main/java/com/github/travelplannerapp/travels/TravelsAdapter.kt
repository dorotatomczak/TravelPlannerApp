package com.github.travelplannerapp.travels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.travelplannerapp.R
import com.github.travelplannerapp.deleteactionmode.DeleteActionModeToolbar
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_travel.*

class TravelsAdapter(val presenter: TravelsContract.Presenter) : RecyclerView.Adapter<TravelsAdapter.TravelsViewHolder>() {

    private var actionMode: ActionMode? = null
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

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
        actionMode?.finish()
        actionMode = null
    }

    inner class TravelsViewHolder(val presenter: TravelsContract.Presenter, override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer, TravelsContract.TravelItemView, View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)
            checkboxItemTravel.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                    fun(_: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            presenter.addTravelIdToDelete(adapterPosition)
                        } else {
                            presenter.removeTravelIdToDelete(adapterPosition)
                        }
                    }
            ))
        }

        override fun onClick(v: View?) {
            if (actionMode != null) checkboxItemTravel.isChecked = !checkboxItemTravel.isChecked
            else presenter.openTravelDetails(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            if (actionMode == null) actionMode = (containerView.context as AppCompatActivity)
                    .startSupportActionMode(DeleteActionModeToolbar(presenter))
            return true
        }

        override fun setName(name: String) {
            textViewItemTravelName.text = name
        }

        override fun setImage(url: String) {
            Glide.with(this.itemView.context)
                    .apply { requestOptions }
                    .load(url)
                    .into(imageViewItemTravel)
        }

        override fun setCheckbox() {
            if (actionMode != null) checkboxItemTravel.visibility = View.VISIBLE
            else checkboxItemTravel.visibility = View.GONE

            checkboxItemTravel.isChecked = false
        }
    }
}
