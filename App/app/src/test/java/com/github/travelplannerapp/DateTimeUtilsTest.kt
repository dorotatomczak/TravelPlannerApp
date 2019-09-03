package com.github.travelplannerapp

import com.github.travelplannerapp.utils.DateTimeUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DateTimeUtilsTest {

    @Test
    fun `Should return true when datetime A is before datetime B`() {
        // default pattern
        val dateA = "SEP 10, 2019"
        val timeA = "10:15 AM"
        val dateB = "SEP 15, 2019"
        val timeB = "1:35 PM"

        val result = DateTimeUtils.isDateTimeABeforeDateTimeB(dateA, timeA, dateB, timeB)

        assertTrue(result)
    }

    @Test
    fun `Should return false when datetime A is after datetime B`() {
        // default pattern
        val dateA = "SEP 15, 2019"
        val timeA = "10:15 AM"
        val dateB = "SEP 15, 2019"
        val timeB = "1:35 AM"

        val result = DateTimeUtils.isDateTimeABeforeDateTimeB(dateA, timeA, dateB, timeB)

        assertFalse(result)
    }

    @Test
    fun `Should return false when datetime A is the same as datetime B`() {
        // default pattern
        val dateA = "SEP 10, 2019"
        val timeA = "10:15 AM"
        val dateB = "SEP 10, 2019"
        val timeB = "10:15 AM"

        val result = DateTimeUtils.isDateTimeABeforeDateTimeB(dateA, timeA, dateB, timeB)

        assertFalse(result)
    }
}
