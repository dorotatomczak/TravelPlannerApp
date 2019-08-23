package com.github.travelplannerapp.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ticket.*

class TicketsAdapter(val presenter: TicketsContract.Presenter) : RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket, parent, false)); }

    override fun getItemCount(): Int {
        return presenter.getTicketsCount()
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        presenter.onBindTravelsAtPosition(position, holder)
    }

    inner class TicketsViewHolder(val presenter: TicketsContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, TicketsContract.TicketItemView, View.OnClickListener {

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //TODO [Dorota] Open full image
        }

        override fun setImage(url: String) {
            Glide.with(this.itemView.context)
                    .load(url)
                    .into(imageViewItemTicket)
        }
    }
}
