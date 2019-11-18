package com.github.travelplannerapp.scans

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
import kotlinx.android.synthetic.main.item_scan.*

class ScansAdapter(val presenter: ScansContract.Presenter) : RecyclerView.Adapter<ScansAdapter.ScansViewHolder>() {

    private var actionMode: ActionMode? = null
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScansViewHolder {
        return ScansViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scan, parent, false)); }

    override fun getItemCount(): Int {
        return presenter.getScansCount()
    }

    override fun onBindViewHolder(holder: ScansViewHolder, position: Int) {
        presenter.onBindScansAtPosition(position, holder)
    }

    fun leaveActionMode() {
        actionMode?.finish()
        actionMode = null
    }

    inner class ScansViewHolder(val presenter: ScansContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, ScansContract.ScanItemView, View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)

            checkboxItemScan.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                    fun(_: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            presenter.addScanToDelete(adapterPosition)
                        } else {
                            presenter.removeScanToDelete(adapterPosition)
                        }
                    }
            ))
        }

        override fun onClick(v: View?) {
            if (actionMode != null) checkboxItemScan.isChecked = !checkboxItemScan.isChecked
            else presenter.onScanClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            if (actionMode == null) actionMode = (containerView.context as AppCompatActivity)
                    .startSupportActionMode(DeleteActionModeToolbar(presenter))
            return true
        }

        override fun setImage(url: String) {
            Glide.with(this.itemView.context)
                    .apply { requestOptions }
                    .load(url)
                    .into(imageViewItemScan)
        }

        override fun setCheckbox() {
            if (actionMode != null) checkboxItemScan.visibility = View.VISIBLE
            else checkboxItemScan.visibility = View.GONE

            checkboxItemScan.isChecked = false
        }
    }
}
