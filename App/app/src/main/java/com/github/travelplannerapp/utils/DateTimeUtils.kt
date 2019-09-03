package com.github.travelplannerapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun isDateTimeABeforeDateTimeB(dateA: String, timeA: String, dateB:String, timeB: String) : Boolean {
        val calendarA = stringToDateTime(dateA, timeA)
        val calendarB = stringToDateTime(dateB, timeB)

        return calendarA.compareTo(calendarB) == -1
    }

    fun dateToString(calendar: Calendar) : String {
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(calendar.time)
    }

    fun timeToString(calendar: Calendar) : String {
        val formatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        return formatter.format(calendar.time)
    }

    private fun stringToDateTime(date: String, time: String) : Calendar {
        val dateTimeText = "$date $time"
        val calendar = GregorianCalendar.getInstance()
        val formatter = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.DEFAULT, SimpleDateFormat.SHORT)

        formatter.parse(dateTimeText)?.let { calendar.time = it }

        return calendar
    }

}
