# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Shows the D-day label of `task` in `textView`, using `LocalDate.now()` as today. When there is a label (`"D-N"`, `"D-Day"`, `"D+N"` or `"CLEAR"`), the text is set to it and the view is `View.VISIBLE`. When there is no label (`task` is null, or the task is active with no due date), no label is shown: the text is `""` and the view is `View.GONE`, whatever the view showed before. The same `TextView` is reused when a task is checked or unchecked and when list rows are recycled, so every call must fully replace the previous label.

**Pre-conditions**
- Called on the main thread with a `TextView` created from an app context.
- In tests, `today` means `LocalDate.now()` read once at the start of the test, and every due date is written relative to it (`today.plusDays(n)`), so the test gives the same result on any day.

**Post-conditions**
- Success cases
  - A completed task with a due date shows `"CLEAR"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `View.GONE`, and `task = Task(title = "Task 1", isCompleted = true, dueDate = today.plusDays(3))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_completedTaskWithDueDate_showsClear`
  - A completed task without a due date shows `"CLEAR"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `View.GONE`, and `task = Task(title = "Task 2", isCompleted = true, dueDate = null)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_completedTaskWithoutDueDate_showsClear`
  - An active task due in N days shows `"D-N"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `View.GONE`, and `task = Task(title = "Task 3", isCompleted = false, dueDate = today.plusDays(3))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-3"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_activeTaskDueInThreeDays_showsDMinus3`
  - An active task without a due date hides a label that was shown before. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `View.VISIBLE`, and `task = Task(title = "Task 4", isCompleted = false, dueDate = null)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `""` and `textView.visibility` is `View.GONE` (8). Test: `TasksListBindingsTest.setDueDateLabel_activeTaskWithoutDueDate_hidesLabel`
  - A null task hides a label that was shown before. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `View.VISIBLE`, and `task = null` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `""` and `textView.visibility` is `View.GONE` (8). Test: `TasksListBindingsTest.setDueDateLabel_nullTask_hidesLabel`
- Error cases
  - None.
- Edge cases
  - Boundary between due and overdue (0 days): due today shows `"D-Day"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `View.GONE`, and `task = Task(title = "Task 5", isCompleted = false, dueDate = today)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-Day"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_activeTaskDueToday_showsDDay`
  - Right after the 0-day boundary: due tomorrow shows `"D-1"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `View.GONE`, and `task = Task(title = "Task 6", isCompleted = false, dueDate = today.plusDays(1))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-1"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_activeTaskDueTomorrow_showsDMinus1`
  - Right before the 0-day boundary: overdue by 1 day shows `"D+1"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `View.GONE`, and `task = Task(title = "Task 7", isCompleted = false, dueDate = today.minusDays(1))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D+1"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_activeTaskOverdueOneDay_showsDPlus1`
  - Unchecking a completed task without a due date removes `"CLEAR"`. `[Integration]`
    - Tip: Given a `TextView` that was bound with `Task(title = "Task 8", isCompleted = true, dueDate = null)` so it shows `"CLEAR"` and `View.VISIBLE` / When `setDueDateLabel(textView, Task(title = "Task 8", isCompleted = false, dueDate = null))` is called on the same `TextView` / Then `textView.text` is `""` and `textView.visibility` is `View.GONE` (8). Test: `TasksListBindingsTest.setDueDateLabel_uncheckCompletedTaskWithoutDueDate_hidesLabel`
  - Unchecking a completed task with a due date replaces `"CLEAR"` with the D-day label. `[Integration]`
    - Tip: Given a `TextView` that was bound with `Task(title = "Task 9", isCompleted = true, dueDate = today.plusDays(3))` so it shows `"CLEAR"` / When `setDueDateLabel(textView, Task(title = "Task 9", isCompleted = false, dueDate = today.plusDays(3)))` is called on the same `TextView` / Then `textView.text` is `"D-3"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_uncheckCompletedTaskWithDueDate_showsDMinus3`
  - Checking an active task with a due date replaces the D-day label with `"CLEAR"`. `[Integration]`
    - Tip: Given a `TextView` that was bound with `Task(title = "Task 10", isCompleted = false, dueDate = today.plusDays(3))` so it shows `"D-3"` / When `setDueDateLabel(textView, Task(title = "Task 10", isCompleted = true, dueDate = today.plusDays(3)))` is called on the same `TextView` / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE` (0). Test: `TasksListBindingsTest.setDueDateLabel_checkActiveTaskWithDueDate_showsClear`

**Undefined behavior**
- The date changing at midnight between two calls in the same test is not covered; tests read `LocalDate.now()` once and must not run across midnight.

2026년 10월 17일 (토) 오전 12:17, 남경민 <winnie2001@naver.com>님이 작성:
(base) PS C:\Users\winni\SWPP-2026-Testing-Practice> Get-Content specs\DueDateUtils.spec.md
# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Calculates and returns a formatted D-day label string representing the relative due date status for a task based on whether it is completed, its due date, and the reference date `today`.

**Pre-conditions**
- `today` must not be null.

**Post-conditions**
- Success cases
  - Completed task returns `"CLEAR"`, regardless of `dueDate` being set. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = true`, and `dueDate = 2026-10-20` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"CLEAR"` is returned for test `DueDateUtilsTest.dueDateLabel_completedTask_returnsClear`.
  - Completed task with no due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = true`, and `dueDate = null` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"CLEAR"` is returned for test `DueDateUtilsTest.dueDateLabel_completedTaskNullDueDate_returnsClear`.
  - Active task with no due date returns `""`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = false`, and `dueDate = null` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `""` is returned for test `DueDateUtilsTest.dueDateLabel_nullDueDateActiveTask_returnsEmptyString`.
  - Active task due in multiple days (N > 0 calendar days after today) returns `"D-N"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = false`, and `dueDate = 2026-10-22` (5 calendar days later) / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D-5"` is returned for test `DueDateUtilsTest.dueDateLabel_futureDueDateActiveTask_returnsDMinusN`.
  - Active task overdue by multiple days (N > 0 calendar days before today) returns `"D+N"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = false`, and `dueDate = 2026-10-12` (5 calendar days earlier) / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D+5"` is returned for test `DueDateUtilsTest.dueDateLabel_pastDueDateActiveTask_returnsDPlusN`.
- Error cases
  - None. `[Unit]`
    - Tip: Given any valid inputs / When `dueDateLabel` is called / Then no exception is thrown for test `DueDateUtilsTest.dueDateLabel_validInputs_noExceptionThrown`.
- Edge cases
  - Active task due today (`dueDate == today`) returns `"D-Day"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = false`, and `dueDate = 2026-10-17` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D-Day"` is returned for test `DueDateUtilsTest.dueDateLabel_dueDateTodayActiveTask_returnsDDay`.
  - Active task overdue by exactly 1 day (`dueDate` is 1 day before `today`) returns `"D+1"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = false`, and `dueDate = 2026-10-16` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D+1"` is returned for test `DueDateUtilsTest.dueDateLabel_overdueOneDayActiveTask_returnsDPlusOne`.
  - Active task due in exactly 1 day (`dueDate` is 1 day after `today`) returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `isCompleted = false`, and `dueDate = 2026-10-18` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D-1"` is returned for test `DueDateUtilsTest.dueDateLabel_dueTomorrowActiveTask_returnsDMinusOne`.
  - Active task due across month boundary (1 day after month end) returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-10-31`, `isCompleted = false`, and `dueDate = 2026-11-01` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D-1"` is returned for test `DueDateUtilsTest.dueDateLabel_monthBoundaryDueTomorrowActiveTask_returnsDMinusOne`.
  - Active task due across year boundary (1 day after year end) returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-12-31`, `isCompleted = false`, and `dueDate = 2027-01-01` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D-1"` is returned for test `DueDateUtilsTest.dueDateLabel_yearBoundaryDueTomorrowActiveTask_returnsDMinusOne`.
  - Active task due across leap day boundary (Feb 28 to Feb 29 in leap year) returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2028-02-28`, `isCompleted = false`, and `dueDate = 2028-02-29` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then `"D-1"` is returned for test `DueDateUtilsTest.dueDateLabel_leapYearDueTomorrowActiveTask_returnsDMinusOne`.

**Undefined behavior**
- None.





(base) PS C:\Users\winni\SWPP-2026-Testing-Practice> Get-Content specs\TasksListBindings.spec.md
# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Updates the `TextView`'s text with the due date label calculated for `task` relative to current date (`LocalDate.now()`) and makes the `TextView` visible (`View.VISIBLE`) when the label is non-empty; leaves the `TextView` unchanged when `task` is null or produces an empty label.

**Pre-conditions**
- `textView` must not be null.

**Post-conditions**
- Success cases
  - When `task` is non-null and produces a non-empty due date label, `textView.text` is set to the label string and `textView.visibility` is set to `View.VISIBLE`. `[Integration]`
    - Tip: Given `textView` with initial text `""` and visibility `View.GONE`, and `task = Task(title = "Task 1", isCompleted = true, dueDate = LocalDate.of(2026, 10, 17))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE` (value `0`) for test `TasksListBindingsTest.setDueDateLabel_taskWithLabel_setsTextAndVisibilityVisible`.
  - When `task` is null, `textView.text` and `textView.visibility` remain unchanged. `[Integration]`
    - Tip: Given `textView` with initial text `"Initial"` and visibility `View.GONE`, and `task = null` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` remains `"Initial"` and `textView.visibility` remains `View.GONE` (value `8`) for test `TasksListBindingsTest.setDueDateLabel_nullTask_leavesTextViewUnchanged`.
  - When `task` is an active task with no due date (producing an empty label `""`), `textView.text` and `textView.visibility` remain unchanged. `[Integration]`
    - Tip: Given `textView` with initial text `"Initial"` and visibility `View.GONE`, and `task = Task(title = "Task 2", isCompleted = false, dueDate = null)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` remains `"Initial"` and `textView.visibility` remains `View.GONE` (value `8`) for test `TasksListBindingsTest.setDueDateLabel_taskWithEmptyLabel_leavesTextViewUnchanged`.
- Error cases
  - None. `[Integration]`
    - Tip: Given any valid inputs / When `setDueDateLabel` is called / Then no exception is thrown for test `TasksListBindingsTest.setDueDateLabel_validInputs_noExceptionThrown`.
- Edge cases
  - Active task due today sets text `"D-Day"` and makes `TextView` visible. `[Integration]`
    - Tip: Given `textView` with initial text `""` and visibility `View.GONE`, and `task = Task(title = "Task 3", isCompleted = false, dueDate = LocalDate.of(2026, 10, 17))` when `LocalDate.now()` is `2026-10-17` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-Day"` and `textView.visibility` is `View.VISIBLE` (value `0`) for test `TasksListBindingsTest.setDueDateLabel_activeTaskDueToday_setsDDayAndVisible`.
  - Active task due in 1 day sets text `"D-1"` and makes `TextView` visible. `[Integration]`
    - Tip: Given `textView` with initial text `""` and visibility `View.GONE`, and `task = Task(title = "Task 4", isCompleted = false, dueDate = LocalDate.of(2026, 10, 18))` when `LocalDate.now()` is `2026-10-17` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-1"` and `textView.visibility` is `View.VISIBLE` (value `0`) for test `TasksListBindingsTest.setDueDateLabel_activeTaskDueTomorrow_setsDMinusOneAndVisible`.
  - Active task overdue by 1 day sets text `"D+1"` and makes `TextView` visible. `[Integration]`
    - Tip: Given `textView` with initial text `""` and visibility `View.GONE`, and `task = Task(title = "Task 5", isCompleted = false, dueDate = LocalDate.of(2026, 10, 16))` when `LocalDate.now()` is `2026-10-17` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D+1"` and `textView.visibility` is `View.VISIBLE` (value `0`) for test `TasksListBindingsTest.setDueDateLabel_activeTaskOverdueOneDay_setsDPlusOneAndVisible`.

**Undefined behavior**
- None.