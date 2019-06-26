package com.github.travelplannerapp.traveldetails

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.*
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tile.*

class TravelDetailsAdapter (val presenter: TravelDetailsContract.Presenter): RecyclerView.Adapter<TravelDetailsAdapter.TravelDetailsViewHolder>(){

    //TODO([Dorota] Possibly change icons, names, colors in the future, these are temporary)
    //TODO([Dorota] Adjust heights to user's screen height)
    //TODO([Dorota] Investigate how to better store these values and where, should the adapter communicate with presenter or is it unnecessary)
    var tileImages = listOf(R.drawable.ic_place, R.drawable.ic_plane, R.drawable.ic_hotel, R.drawable.ic_ticket)
    var tileColors = listOf(R.color.sunsetOrange, R.color.moonstoneBlue, R.color.raspberryGlace, R.color.neonCarrot)
    var tileNames = listOf(R.string.day_plans, R.string.transport, R.string.accommodation, R.string.tickets)
    var tileHeights = listOf(500, 800, 1000, 700)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelDetailsViewHolder {
        return TravelDetailsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tile, parent, false));    }

    override fun getItemCount(): Int {
        return tileNames.size
    }

    override fun onBindViewHolder(holder: TravelDetailsViewHolder, position: Int) {
        presenter.onBindTileAtPosition(position, holder)
    }

    inner class TravelDetailsViewHolder(val presenter: TravelDetailsContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer, TravelDetailsContract.TileItemView, View.OnClickListener {

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setBackgroundColor(tileIndex: Int) {
            linearLayoutItemTile.setBackgroundColor(ContextCompat.getColor(containerView.context, tileColors[tileIndex]))
        }

        override fun setImage(tileIndex: Int) {
            imageViewItemTile.setImageDrawable(ContextCompat.getDrawable(containerView.context, tileImages[tileIndex]))
        }

        override fun setName(tileIndex: Int) {
            textViewItemTileName.text = containerView.context.getString(tileNames[tileIndex])
        }

        override fun setHeight(tileIndex: Int) {
            cardViewItemTile.layoutParams =  LayoutParams(LayoutParams.MATCH_PARENT, tileHeights[tileIndex])
        }
    }
}