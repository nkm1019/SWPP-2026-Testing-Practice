package com.example.swpp.tododapp.statistics

import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StatisticsUtilsParameterizedTest(
    private val name: String,
    private val tasks: List<Task>,
    private val expectedActive: Float,
    private val expectedCompleted: Float
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("one active, one completed",
                listOf(Task("t", "d", true), Task("t", "d", false)), 50f, 50f),
            arrayOf("empty list",
                emptyList<Task>(), 0f, 0f),
            arrayOf("two completed",
                listOf(Task("t", "d", true), Task("t", "d", true)), 0f, 100f)
        )
    }

    @Test
    fun getActiveAndCompletedStats_parameterized_returnExpected() {
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(expectedActive, result.activeTasksPercent)
        assertEquals(expectedCompleted, result.completedTasksPercent)
    }
}