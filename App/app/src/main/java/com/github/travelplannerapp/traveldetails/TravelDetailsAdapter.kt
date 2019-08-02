package com.github.travelplannerapp.traveldetails

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.*
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tile.*

class TravelDetailsAdapter (val presenter: TravelDetailsContract.Presenter): RecyclerView.Adapter<TravelDetailsAdapter.TravelDetailsViewHolder>(){

    //TODO([Dorota] Adjust heights to fill remaining space on the screen)
    //TODO([Dorota] Investigate how to better store these values and where, should the adapter communicate with presenter or is it unnecessary)
    var categories = listOf(Category(Category.CategoryType.DAY_PLANS, R.string.day_plans, R.drawable.ic_place, R.color.sunsetOrange, 1000),
            Category(Category.CategoryType.TRANSPORT, R.string.transport, R.drawable.ic_plane, R.color.moonstoneBlue, 700),
            Category(Category.CategoryType.ACCOMMODATION, R.string.accommodation, R.drawable.ic_hotel, R.color.raspberryGlace, 800),
            Category(Category.CategoryType.TICKETS, R.string.tickets, R.drawable.ic_ticket, R.color.neonCarrot, 500))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelDetailsViewHolder {
        return TravelDetailsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tile, parent, false));    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: TravelDetailsViewHolder, position: Int) {
        holder.setName(position)
        holder.setImage(position)
        holder.setMinHeight(position)
        holder.setBackgroundColor(position)
    }

    inner class TravelDetailsViewHolder(val presenter: TravelDetailsContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer, TravelDetailsContract.TileItemView, View.OnClickListener {

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            presenter.openCategory(categories[adapterPosition].type)
        }

        override fun setBackgroundColor(tileIndex: Int) {
            linearLayoutItemTile.setBackgroundColor(ContextCompat.getColor(containerView.context, categories[tileIndex].color))
        }

        override fun setImage(tileIndex: Int) {
            imageViewItemTile.setImageDrawable(ContextCompat.getDrawable(containerView.context, categories[tileIndex].icon))
        }

        override fun setName(tileIndex: Int) {
            textViewItemTileName.text = containerView.context.getString(categories[tileIndex].name)
        }

        override fun setMinHeight(tileIndex: Int) {
            cardViewItemTile.layoutParams =  LayoutParams(LayoutParams.MATCH_PARENT, categories[tileIndex].minHeight)
        }
    }
}