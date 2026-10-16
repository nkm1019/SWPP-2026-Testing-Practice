package com.example.swpp.tododapp.tasks

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    @Test
    fun setDueDateLabel_completedTaskWithDueDate_showsClear() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = ""
                visibility = View.GONE
            }
            val task = Task(title = "Task 1", isCompleted = true, dueDate = today.plusDays(3))

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_completedTaskWithoutDueDate_showsClear() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = ""
                visibility = View.GONE
            }
            val task = Task(title = "Task 2", isCompleted = true, dueDate = null)

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeTaskDueInThreeDays_showsDMinus3() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = ""
                visibility = View.GONE
            }
            val task = Task(title = "Task 3", isCompleted = false, dueDate = today.plusDays(3))

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("D-3", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeTaskWithoutDueDate_hidesLabel() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = "D-3"
                visibility = View.VISIBLE
            }
            val task = Task(title = "Task 4", isCompleted = false, dueDate = null)

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_nullTask_hidesLabel() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = "D-3"
                visibility = View.VISIBLE
            }
            val task: Task? = null

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeTaskDueToday_showsDDay() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = ""
                visibility = View.GONE
            }
            val task = Task(title = "Task 5", isCompleted = false, dueDate = today)

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("D-Day", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeTaskDueTomorrow_showsDMinus1() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = ""
                visibility = View.GONE
            }
            val task = Task(title = "Task 6", isCompleted = false, dueDate = today.plusDays(1))

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeTaskOverdueOneDay_showsDPlus1() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context).apply {
                text = ""
                visibility = View.GONE
            }
            val task = Task(title = "Task 7", isCompleted = false, dueDate = today.minusDays(1))

            // WHEN
            setDueDateLabel(textView, task)

            // THEN
            assertEquals("D+1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_uncheckCompletedTaskWithoutDueDate_hidesLabel() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context)
            setDueDateLabel(textView, Task(title = "Task 8", isCompleted = true, dueDate = null))

            // WHEN
            setDueDateLabel(textView, Task(title = "Task 8", isCompleted = false, dueDate = null))

            // THEN
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_uncheckCompletedTaskWithDueDate_showsDMinus3() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context)
            setDueDateLabel(textView, Task(title = "Task 9", isCompleted = true, dueDate = today.plusDays(3)))

            // WHEN
            setDueDateLabel(textView, Task(title = "Task 9", isCompleted = false, dueDate = today.plusDays(3)))

            // THEN
            assertEquals("D-3", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_checkActiveTaskWithDueDate_showsClear() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val today = LocalDate.now()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // GIVEN
            val textView = TextView(context)
            setDueDateLabel(textView, Task(title = "Task 10", isCompleted = false, dueDate = today.plusDays(3)))

            // WHEN
            setDueDateLabel(textView, Task(title = "Task 10", isCompleted = true, dueDate = today.plusDays(3)))

            // THEN
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }
}
