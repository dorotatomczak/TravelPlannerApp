package com.github.travelplannerapp.dayplans

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.deleteactionmode.DeleteActionModeToolbar
import com.github.travelplannerapp.utils.DateTimeUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_plan_date_separator.*
import kotlinx.android.synthetic.main.item_plan_element.*

class DayPlansAdapter(val presenter: DayPlansContract.Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var actionMode: ActionMode? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            DayPlansContract.DayPlanItem.TYPE_PLAN -> PlanElementViewHolder(presenter, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_plan_element, parent, false))

            DayPlansContract.DayPlanItem.TYPE_DATE -> DateSeparatorViewHolder(presenter, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_plan_date_separator, parent, false))

            else -> throw Exception("There is no ViewHolder that matches the type $viewType")
        }
    }

    override fun getItemCount(): Int {
        return presenter.getDayPlanItemsCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            DayPlansContract.DayPlanItem.TYPE_PLAN -> presenter.onBindDayPlanItemAtPosition(position, holder as PlanElementViewHolder)
            DayPlansContract.DayPlanItem.TYPE_DATE -> presenter.onBindDayPlanItemAtPosition(position, holder as DateSeparatorViewHolder)
            else -> throw Exception("There is no method that would bind ViewHolder with type ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return presenter.getDayPlanItemType(position)
    }

    fun leaveActionMode() {
        actionMode = null
    }

    inner class DateSeparatorViewHolder(val presenter: DayPlansContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, DayPlansContract.DateSeparatorItemView {

        override fun setDate(date: String) {
            textViewPlanDateSeparator.text = date
        }

    }

    inner class PlanElementViewHolder(val presenter: DayPlansContract.Presenter, override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer, DayPlansContract.PlanElementItemView, View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)
            checkboxItemPlanElement.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                    fun(_: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            presenter.addPlanElementIdToDelete(adapterPosition)
                        } else {
                            presenter.removePlanElementIdToDelete(adapterPosition)
                        }
                    }
            ))
        }

        override fun onClick(v: View?) {
            //TODO [Dorota] Show planElement details
        }

        override fun onLongClick(v: View?): Boolean {
            if (actionMode == null) actionMode = (containerView.context as AppCompatActivity)
                    .startSupportActionMode(DeleteActionModeToolbar(presenter))
            return true
        }

        override fun setName(name: String) {
            textViewItemPlanName.text = name
        }

        override fun setFromTime(time: String) {
            textViewItemPlanFromTime.text = DateTimeUtils.addLeadingZeroToTime(DateFormat.is24HourFormat(containerView.context), time)
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

        override fun setCheckbox() {
            if (actionMode != null) checkboxItemPlanElement.visibility = View.VISIBLE
            else checkboxItemPlanElement.visibility = View.INVISIBLE

            checkboxItemPlanElement.isChecked = false
        }
    }
}
