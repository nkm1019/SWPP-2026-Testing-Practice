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
