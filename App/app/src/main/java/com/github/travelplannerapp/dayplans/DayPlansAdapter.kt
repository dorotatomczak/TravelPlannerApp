package com.github.travelplannerapp.dayplans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_plan.*
import kotlinx.android.synthetic.main.item_plan_date_separator.*

class DayPlansAdapter(val presenter: DayPlansContract.Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            DayPlansContract.DayPlanItem.TYPE_PLAN -> PlanElementViewHolder(presenter, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_plan, parent, false))

            DayPlansContract.DayPlanItem.TYPE_DATE -> PlanDateSeparatorViewHolder(presenter, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_plan_date_separator, parent, false))

            else -> throw Exception("There is no ViewHolder that matches the type $viewType")
        }
    }

    override fun getItemCount(): Int {
        return presenter.getPlanItemsCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            DayPlansContract.DayPlanItem.TYPE_PLAN -> presenter.onBindPlanItemAtPosition(position, holder as PlanElementViewHolder)
            DayPlansContract.DayPlanItem.TYPE_DATE -> presenter.onBindPlanItemAtPosition(position, holder as PlanDateSeparatorViewHolder)
            else -> throw Exception("There is no method that would bind ViewHolder with type ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return presenter.getPlanItemType(position)
    }

    inner class PlanDateSeparatorViewHolder(val presenter: DayPlansContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, DayPlansContract.PlanDateSeparatorItemView {

        override fun setDate(date: String) {
            textViewPlanDateSeparator.text = date
        }

    }

    inner class PlanElementViewHolder(val presenter: DayPlansContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, DayPlansContract.PlanElementItemView, View.OnClickListener {

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //TODO [Dorota] Show plan details
        }

        override fun setName(name: String) {
            textViewItemPlanName.text = name
        }

        override fun setFromTime(time: String) {
            textViewItemPlanFromTime.text = time
        }

        override fun setIcon(icon: Int) {
            imageViewItemPlan.setImageDrawable(ContextCompat.getDrawable(containerView.context, icon))
        }

        override fun setLocation(location: String) {
            textViewItemPlanLocation.text = location
        }

        override fun showLine() {
            lineItemPlan.visibility = View.VISIBLE
        }

        override fun hideLine() {
            lineItemPlan.visibility = View.INVISIBLE
        }
    }
}
