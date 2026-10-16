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
