package com.github.travelplannerapp.tickets

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
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ticket.*

class TicketsAdapter(val presenter: TicketsContract.Presenter) : RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder>() {

    private var actionMode: ActionMode? = null
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket, parent, false)); }

    override fun getItemCount(): Int {
        return presenter.getTicketsCount()
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        presenter.onBindTicketsAtPosition(position, holder)
    }

    fun leaveActionMode() {
        actionMode = null
    }

    inner class TicketsViewHolder(val presenter: TicketsContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, TicketsContract.TicketItemView, View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)

            checkboxItemTicket.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                    fun(_: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            presenter.addTicketToDelete(adapterPosition)
                        } else {
                            presenter.removeTicketToDelete(adapterPosition)
                        }
                    }
            ))
        }

        override fun onClick(v: View?) {
            if (actionMode == null) presenter.onScanClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            actionMode = (containerView.context as AppCompatActivity)
                    .startSupportActionMode(TicketsActionModeToolbar(presenter))
            return true
        }

        override fun setImage(url: String) {
            Glide.with(this.itemView.context)
                    .apply { requestOptions }
                    .load(url)
                    .into(imageViewItemTicket)
        }

        override fun setCheckbox() {
            if (actionMode != null) checkboxItemTicket.visibility = View.VISIBLE
            else checkboxItemTicket.visibility = View.GONE

            checkboxItemTicket.isChecked = false
        }
    }
}
