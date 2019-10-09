package com.github.travelplannerapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    private const val TWELVE_HOUR_FORMAT = "HH:mm AM"

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

    fun dateToString(dateTimeMs: Long) : String {
        val calendar = longToDateTime(dateTimeMs)
        return dateToString(calendar)
    }

    fun timeToString(dateTimeMs: Long) : String {
        val calendar = longToDateTime(dateTimeMs)
        return timeToString(calendar)
    }

    fun stringToDateTime(date: String, time: String) : Calendar {
        val dateTimeText = "$date $time"
        val calendar = GregorianCalendar.getInstance()
        val formatter = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.DEFAULT, SimpleDateFormat.SHORT)

        formatter.parse(dateTimeText)?.let { calendar.time = it }

        return calendar
    }

    fun longToDateTime(dateTimeMs: Long) : Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = dateTimeMs

        return calendar
    }

    fun addLeadingZeroToTime(is24HourFormat: Boolean, time: String): String {

        return when (!is24HourFormat && time.count() < TWELVE_HOUR_FORMAT.count()) {
            true -> "0$time"
            else -> time
        }
    }
}
