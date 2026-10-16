# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Returns the D-day label shown next to a task in the list. A completed task shows `"CLEAR"` with or without a due date, and an active task with no due date shows no label (`""`). Otherwise N is the total number of calendar days between `today` and `dueDate`, however many months or years apart they are: `"D-N"` when due in N days, `"D-Day"` when due today, `"D+N"` when overdue by N days.

**Pre-conditions**
- `today` is the reference date (the caller passes the current date).
- `dueDate` may be null, meaning the task has no due date.

**Post-conditions**
- Success cases
  - A completed task with a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-10-20`, `isCompleted = true` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"CLEAR"`. Test: `DueDateUtilsTest.dueDateLabel_completedTask_returnsClear`
  - A completed task without a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = null`, `isCompleted = true` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"CLEAR"`. Test: `DueDateUtilsTest.dueDateLabel_completedTaskNullDueDate_returnsClear`
  - An active task without a due date returns `""` (no label). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = null`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `""`. Test: `DueDateUtilsTest.dueDateLabel_nullDueDateActiveTask_returnsEmptyString`
  - An active task due in N days in the same month returns `"D-N"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-10-22`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-5"`. Test: `DueDateUtilsTest.dueDateLabel_futureDueDateActiveTask_returnsDMinusN`
  - An active task overdue by N days in the same month returns `"D+N"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-10-12`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+5"`. Test: `DueDateUtilsTest.dueDateLabel_pastDueDateActiveTask_returnsDPlusN`
  - An active task due more than one month later counts all calendar days, not only the day-of-month difference. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-11-20`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-34"`. Test: `DueDateUtilsTest.dueDateLabel_dueMoreThanOneMonthLater_returnsTotalDays`
  - An active task overdue by more than one month counts all calendar days. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-09-10`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+37"`. Test: `DueDateUtilsTest.dueDateLabel_overdueMoreThanOneMonth_returnsTotalDays`
  - An active task due exactly one year later counts all calendar days. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2027-10-17`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-365"`. Test: `DueDateUtilsTest.dueDateLabel_dueOneYearLater_returnsDMinus365`
- Error cases
  - None.
- Edge cases
  - Boundary between due and overdue (0 days): due today returns `"D-Day"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-10-17`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-Day"`. Test: `DueDateUtilsTest.dueDateLabel_dueDateTodayActiveTask_returnsDDay`
  - Right after the 0-day boundary: due in 1 day returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-10-18`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_dueTomorrowActiveTask_returnsDMinusOne`
  - Right before the 0-day boundary: overdue by 1 day returns `"D+1"`. `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-10-16`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+1"`. Test: `DueDateUtilsTest.dueDateLabel_overdueOneDayActiveTask_returnsDPlusOne`
  - One-month boundary, future side, just before one month (30 days). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-11-16`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-30"`. Test: `DueDateUtilsTest.dueDateLabel_dueJustUnderOneMonth_returnsDMinus30`
  - One-month boundary, future side, exactly one month (31 days). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-11-17`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-31"`. Test: `DueDateUtilsTest.dueDateLabel_dueExactlyOneMonth_returnsDMinus31`
  - One-month boundary, future side, just after one month (32 days). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-11-18`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-32"`. Test: `DueDateUtilsTest.dueDateLabel_dueJustOverOneMonth_returnsDMinus32`
  - One-month boundary, overdue side, just before one month (29 days). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-09-18`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+29"`. Test: `DueDateUtilsTest.dueDateLabel_overdueJustUnderOneMonth_returnsDPlus29`
  - One-month boundary, overdue side, exactly one month (30 days). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-09-17`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+30"`. Test: `DueDateUtilsTest.dueDateLabel_overdueExactlyOneMonth_returnsDPlus30`
  - One-month boundary, overdue side, just after one month (31 days). `[Unit]`
    - Tip: Given `today = 2026-10-17`, `dueDate = 2026-09-16`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+31"`. Test: `DueDateUtilsTest.dueDateLabel_overdueJustOverOneMonth_returnsDPlus31`
  - Crossing a month end by 1 day returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-10-31`, `dueDate = 2026-11-01`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_monthBoundaryDueTomorrowActiveTask_returnsDMinusOne`
  - Crossing a year end by 1 day returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-12-31`, `dueDate = 2027-01-01`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_yearBoundaryDueTomorrowActiveTask_returnsDMinusOne`
  - Crossing a leap day by 1 day returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2028-02-28`, `dueDate = 2028-02-29`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_leapYearDueTomorrowActiveTask_returnsDMinusOne`
  - A one-month span that contains a leap day counts the real number of days (29). `[Unit]`
    - Tip: Given `today = 2028-02-01`, `dueDate = 2028-03-01`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-29"`. Test: `DueDateUtilsTest.dueDateLabel_oneMonthAcrossLeapFebruary_returnsDMinus29`

**Undefined behavior**
- None.
