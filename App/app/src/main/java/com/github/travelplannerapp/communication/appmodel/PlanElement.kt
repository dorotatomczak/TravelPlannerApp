package com.github.travelplannerapp.communication.appmodel

import com.github.travelplannerapp.communication.commonmodel.Place
import com.github.travelplannerapp.utils.DateTimeUtils
import java.io.Serializable

data class PlanElement(
        var id: Int,
        var fromDateTimeMs: Long,
        var placeId: Int,
        var place: Place
) : Serializable, Comparable<PlanElement> {

    override fun compareTo(other: PlanElement): Int {
        val dateTimeThis = DateTimeUtils.longToDateTime(this.fromDateTimeMs)
        val dateTimeOther = DateTimeUtils.longToDateTime(other.fromDateTimeMs)

        return dateTimeThis.compareTo(dateTimeOther)
    }
}
