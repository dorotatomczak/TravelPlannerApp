package com.github.travelplannerapp.travels

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_travel.*

class TravelsAdapter (private val presenter: TravelsContract.Presenter):RecyclerView.Adapter<TravelsAdapter.TravelsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelsViewHolder {
        return TravelsViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_travel, parent, false));    }

    override fun getItemCount(): Int {
        return presenter.getTravelsCount()
    }

    override fun onBindViewHolder(holder: TravelsViewHolder, position: Int) {
        presenter.onBindTravelsAtPosition(position, holder)

    }

    class TravelsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, TravelsContract.TravelItemView  {
        override fun setName(name: String) {
            textViewItemTravelName.text = name
        }
    }
}