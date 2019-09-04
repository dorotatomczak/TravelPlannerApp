package com.github.travelplannerapp.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ticket.*

class TicketsAdapter(val presenter: TicketsContract.Presenter) : RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder>() {

    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

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
            presenter.onScanClicked(adapterPosition)
        }

        override fun setImage(url: String) {
            Glide.with(this.itemView.context)
                    .apply { requestOptions }
                    .load(url)
                    .into(imageViewItemTicket)
        }
    }
}
