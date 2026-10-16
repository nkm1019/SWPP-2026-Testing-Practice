package com.example.swpp.tododapp.statistics

import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_two_return50f50f() {
        // GIVEN - One completed task and one active task
        val tasks = listOf(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false)
        )
        // WHEN - Compute the statistics
        val result = getActiveAndCompletedStats(tasks)
        // THEN - Both percentages are 50%
        assertEquals(50f, result.activeTasksPercent)
        assertEquals(50f, result.completedTasksPercent)

    }

    @Test
    fun getActiveAndCompletedStats_zero_return0f0f() {
        // GIVEN - An empty list
        val tasks = emptyList<Task>()
        // WHEN
        val result = getActiveAndCompletedStats(tasks)
        // THEN - Both are 0%
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(0f, result.completedTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_two_return0f100f() {
        // GIVEN - Two completed tasks
        val tasks = listOf(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true)
        )
        // WHEN
        val result = getActiveAndCompletedStats(tasks)
        // THEN - 0% active, 100% completed
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(100f, result.completedTasksPercent)
    }
}
