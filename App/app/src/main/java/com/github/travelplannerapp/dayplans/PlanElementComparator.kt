package com.github.travelplannerapp.dayplans

import com.github.travelplannerapp.communication.commonmodel.PlanElement
import com.github.travelplannerapp.utils.DateTimeUtils

class PlanElementComparator : Comparator<PlanElement> {

    override fun compare(firstPlanElement: PlanElement?, secondPlanElement: PlanElement?): Int {

        if (firstPlanElement != null && secondPlanElement != null) {
            val dateTimeThis = DateTimeUtils.longToDateTime(firstPlanElement.fromDateTimeMs)
            val dateTimeOther = DateTimeUtils.longToDateTime(secondPlanElement.fromDateTimeMs)

            return dateTimeThis.compareTo(dateTimeOther)
        } else {
            throw UnsupportedOperationException()
        }
    }

}
