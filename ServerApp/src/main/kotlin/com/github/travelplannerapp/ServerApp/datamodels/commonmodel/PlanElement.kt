package com.github.travelplannerapp.communication.commonmodel

import java.io.Serializable
import java.util.*

data class PlanElement(
        var id: Int,
        var fromDateTimeMs: Long,
        var placeId: Int,
        var place: Place,
        var completed: Boolean = false,
        var myRating: Int = 0,
        var notes: String
) : Serializable, Comparable<PlanElement> {

    override fun compareTo(other: PlanElement): Int {
        val dateTimeThis = longToDateTime(this.fromDateTimeMs)
        val dateTimeOther = longToDateTime(other.fromDateTimeMs)

        return dateTimeThis.compareTo(dateTimeOther)
    }

    private fun longToDateTime(dateTimeMs: Long): Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = dateTimeMs

        return calendar
    }
}

