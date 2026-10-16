package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedTask_returnsClear() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 10, 20)
        val isCompleted = true

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedTaskNullDueDate_returnsClear() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate: LocalDate? = null
        val isCompleted = true

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_nullDueDateActiveTask_returnsEmptyString() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate: LocalDate? = null
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("", result)
    }

    @Test
    fun dueDateLabel_futureDueDateActiveTask_returnsDMinusN() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 10, 22)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-5", result)
    }

    @Test
    fun dueDateLabel_pastDueDateActiveTask_returnsDPlusN() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 10, 12)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D+5", result)
    }

    @Test
    fun dueDateLabel_dueMoreThanOneMonthLater_returnsTotalDays() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 11, 20)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-34", result)
    }

    @Test
    fun dueDateLabel_overdueMoreThanOneMonth_returnsTotalDays() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 9, 10)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D+37", result)
    }

    @Test
    fun dueDateLabel_dueOneYearLater_returnsDMinus365() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2027, 10, 17)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-365", result)
    }

    @Test
    fun dueDateLabel_dueDateTodayActiveTask_returnsDDay() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 10, 17)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_dueTomorrowActiveTask_returnsDMinusOne() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 10, 18)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_overdueOneDayActiveTask_returnsDPlusOne() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 10, 16)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D+1", result)
    }

    @Test
    fun dueDateLabel_dueJustUnderOneMonth_returnsDMinus30() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 11, 16)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-30", result)
    }

    @Test
    fun dueDateLabel_dueExactlyOneMonth_returnsDMinus31() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 11, 17)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-31", result)
    }

    @Test
    fun dueDateLabel_dueJustOverOneMonth_returnsDMinus32() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 11, 18)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-32", result)
    }

    @Test
    fun dueDateLabel_overdueJustUnderOneMonth_returnsDPlus29() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 9, 18)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D+29", result)
    }

    @Test
    fun dueDateLabel_overdueExactlyOneMonth_returnsDPlus30() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 9, 17)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D+30", result)
    }

    @Test
    fun dueDateLabel_overdueJustOverOneMonth_returnsDPlus31() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 17)
        val dueDate = LocalDate.of(2026, 9, 16)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D+31", result)
    }

    @Test
    fun dueDateLabel_monthBoundaryDueTomorrowActiveTask_returnsDMinusOne() {
        // GIVEN
        val today = LocalDate.of(2026, 10, 31)
        val dueDate = LocalDate.of(2026, 11, 1)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_yearBoundaryDueTomorrowActiveTask_returnsDMinusOne() {
        // GIVEN
        val today = LocalDate.of(2026, 12, 31)
        val dueDate = LocalDate.of(2027, 1, 1)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_leapYearDueTomorrowActiveTask_returnsDMinusOne() {
        // GIVEN
        val today = LocalDate.of(2028, 2, 28)
        val dueDate = LocalDate.of(2028, 2, 29)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_oneMonthAcrossLeapFebruary_returnsDMinus29() {
        // GIVEN
        val today = LocalDate.of(2028, 2, 1)
        val dueDate = LocalDate.of(2028, 3, 1)
        val isCompleted = false

        // WHEN
        val result = dueDateLabel(dueDate, today, isCompleted)

        // THEN
        assertEquals("D-29", result)
    }
}
