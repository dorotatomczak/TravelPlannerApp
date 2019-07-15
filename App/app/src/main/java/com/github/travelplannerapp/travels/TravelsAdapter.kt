package com.github.travelplannerapp.travels

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_travel.*

class TravelsAdapter (val presenter: TravelsContract.Presenter):RecyclerView.Adapter<TravelsAdapter.TravelsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelsViewHolder {
        return TravelsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_travel, parent, false));    }

    override fun getItemCount(): Int {
        return presenter.getTravelsCount()
    }

    override fun onBindViewHolder(holder: TravelsViewHolder, position: Int) {
        presenter.onBindTravelsAtPosition(position, holder)
    }

    inner class TravelsViewHolder(val presenter: TravelsContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, TravelsContract.TravelItemView, View.OnClickListener {

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            presenter.openTravelDetails(adapterPosition)
        }

        override fun setName(name: String) {
            textViewItemTravelName.text = name
        }
    }
    /*@BindingAdapter({"entries", "layout"})
public static <T> void setEntries(ViewGroup viewGroup,
                                  List<T> entries, int layoutId) {
    viewGroup.removeAllViews();
    if (entries != null) {
        LayoutInflater inflater = (LayoutInflater)
            viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < entries.size(); i++) {
            T entry = entries.get(i);
            ViewDataBinding binding = DataBindingUtil
                .inflate(inflater, layoutId, viewGroup, true);
            binding.setVariable(BR.data, entry);
        }
    }
}*/
}