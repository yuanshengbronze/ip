# UI Test Plan

The `test-ui` skill uses this file as the source of truth for console UI tests.
Each test case records its aim, the console inputs, and the expected output after each input.

```program
java -cp "%REPO_ROOT%\build\classes\java\test;%REPO_ROOT%\build\libs\nico.jar" nico.ConsoleTestAdapter
```

```build
gradlew.bat testClasses shadowJar
```

## Test Case: Assign, replace, and filter priorities

Aim: Verify all priorities, multiple matches, completed tasks, and empty results.

```input
todo report
todo book
addpriority 1 high
addpriority 2 high
showpriority high
addpriority 1 medium
showpriority medium
addpriority 1 low
mark 1
showpriority low
showpriority medium
bye
```

```expected
[T][ ] report
---
[T][ ] book
---
I've set this task's priority to high:
[T][ ][high] report
---
I've set this task's priority to high:
[T][ ][high] book
---
Tasks with priority high:
- [T][ ][high] report
- [T][ ][high] book
---
I've set this task's priority to medium:
[T][ ][medium] report
---
Tasks with priority medium: [T][ ][medium] report
---
I've set this task's priority to low:
[T][ ][low] report
---
I've marked this task as done:
[T][X][low] report
---
Tasks with priority low: [T][X][low] report
---
Tasks with priority medium: None
---
Nice seeing you. Until next time!
```

## Test Case: Reject invalid priority commands

Aim: Verify missing arguments, invalid priorities, extra arguments, and invalid task numbers.

```input
addpriority
addpriority 1
addpriority 1 high extra
addpriority 1 urgent
addpriority one high
addpriority 0 high
addpriority 99 high
showpriority
showpriority urgent
showpriority high extra
bye
```

```expected
Error: Choose a task number and priority.
Try: addpriority TASK_NUMBER PRIORITY
---
Error: Choose a task number and priority.
Try: addpriority TASK_NUMBER PRIORITY
---
Error: Choose a task number and priority.
Try: addpriority TASK_NUMBER PRIORITY
---
Error: Priority must be high, medium, or low.
Try: high, medium, or low
---
Error: A task number must be a whole number.
Try: addpriority TASK_NUMBER
---
Error: That task number is not in the list.
Try: addpriority TASK_NUMBER
---
Error: That task number is not in the list.
Try: addpriority TASK_NUMBER
---
Error: Choose a priority to show.
Try: showpriority PRIORITY
---
Error: Priority must be high, medium, or low.
Try: high, medium, or low
---
Error: Priority must be high, medium, or low.
Try: high, medium, or low
---
Nice seeing you. Until next time!
```

## Test Case: Find tasks case-insensitively

Aim: Verify that `find` returns every task description containing its keyword, regardless of letter case.

```input
todo Borrow Book
deadline return book /by 25-08-2026 1900
find BOOK
bye
```

```expected
Nice! I've added this task:
[T][ ] Borrow Book
---
Nice! I've added this task:
[D][ ] return book (by: Aug 25 2026 19:00)
---
Here are the matching tasks in your list:
- [T][ ] Borrow Book
- [D][ ] return book (by: Aug 25 2026 19:00)
---
Nice seeing you. Until next time!
```

## Test Case: Find with no matching tasks

Aim: Verify that `find` reports no matching tasks when no description contains the keyword.

```input
todo borrow book
find report
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
Here are the matching tasks in your list: None
---
Nice seeing you. Until next time!
```

## Test Case: Reject find without keyword

Aim: Verify that `find` rejects a missing keyword with a usage message.

```input
find
bye
```

```expected
Error: Enter a keyword to search for.
Try: find KEYWORD
---
Nice seeing you. Until next time!
```

## Test Case: Reject unknown command

Aim: Verify that an unrecognized command is rejected instead of being added as a task.

```input
return book
bye
```

```expected
Error: I don't recognise that command.
Try: todo DESCRIPTION, list, mark TASK_NUMBER, or bye
---
Nice seeing you. Until next time!
```

## Test Case: Add a todo task

Aim: Verify that a `todo` command creates a nico.Todo task formatted with `[T]`.

```input
todo borrow book
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
Nice seeing you. Until next time!
```

## Test Case: Add a deadline task

Aim: Verify that a `deadline` command parses the description and `/by` date-time value.

```input
deadline return book /by 25-08-2026 1900
bye
```

```expected
Nice! I've added this task:
[D][ ] return book (by: Aug 25 2026 19:00)
---
Nice seeing you. Until next time!
```

## Test Case: Add an event task

Aim: Verify that an `event` command parses the description, `/from` date-time, and `/to` date-time.

```input
event project meeting /from 25-08-2026 1400 /to 25-08-2026 1600
bye
```

```expected
Nice! I've added this task:
[E][ ] project meeting (from: Aug 25 2026 14:00 to: Aug 25 2026 16:00)
---
Nice seeing you. Until next time!
```

## Test Case: List mixed task types

Aim: Verify that `list` displays nico.Todo, nico.Deadline, and nico.Event tasks with their indices.

```input
todo borrow book
deadline submit report /by 25-08-2026 2300
event project meeting /from 25-08-2026 1400 /to 25-08-2026 1600
list
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
Nice! I've added this task:
[D][ ] submit report (by: Aug 25 2026 23:00)
---
Nice! I've added this task:
[E][ ] project meeting (from: Aug 25 2026 14:00 to: Aug 25 2026 16:00)
---
1. [T][ ] borrow book
2. [D][ ] submit report (by: Aug 25 2026 23:00)
3. [E][ ] project meeting (from: Aug 25 2026 14:00 to: Aug 25 2026 16:00)
---
Nice seeing you. Until next time!
```

## Test Case: Show urgent event and deadline

Aim: Verify that `urgent` displays the incomplete event with the earliest start time and the incomplete deadline with the earliest due time.

```input
event later meeting /from 30-08-2026 1000 /to 30-08-2026 1100
deadline later report /by 30-08-2026 2359
event standup /from 25-08-2026 0900 /to 25-08-2026 0930
deadline submit draft /by 26-08-2026 2359
mark 3
mark 4
urgent
bye
```

```expected
Nice! I've added this task:
[E][ ] later meeting (from: Aug 30 2026 10:00 to: Aug 30 2026 11:00)
---
Nice! I've added this task:
[D][ ] later report (by: Aug 30 2026 23:59)
---
Nice! I've added this task:
[E][ ] standup (from: Aug 25 2026 09:00 to: Aug 25 2026 09:30)
---
Nice! I've added this task:
[D][ ] submit draft (by: Aug 26 2026 23:59)
---
I've marked this task as done:
[E][X] standup (from: Aug 25 2026 09:00 to: Aug 25 2026 09:30)
---
I've marked this task as done:
[D][X] submit draft (by: Aug 26 2026 23:59)
---
Most urgent event: [E][ ] later meeting (from: Aug 30 2026 10:00 to: Aug 30 2026 11:00)
Most urgent deadline: [D][ ] later report (by: Aug 30 2026 23:59)
---
Nice seeing you. Until next time!
```

## Test Case: Show urgent with missing task types

Aim: Verify that `urgent` reports `None` for missing event and deadline task types.

```input
todo borrow book
urgent
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
Most urgent event: None
Most urgent deadline: None
---
Nice seeing you. Until next time!
```

## Test Case: Show tied urgent tasks as bullets

Aim: Verify that `urgent` displays all incomplete events and deadlines that tie for the most urgent time.

```input
event demo /from 25-08-2026 0900 /to 25-08-2026 1000
event interview /from 25-08-2026 0900 /to 25-08-2026 0930
event later meeting /from 30-08-2026 1000 /to 30-08-2026 1100
deadline draft /by 26-08-2026 2359
deadline slides /by 26-08-2026 2359
deadline final report /by 30-08-2026 2359
urgent
bye
```

```expected
Nice! I've added this task:
[E][ ] demo (from: Aug 25 2026 09:00 to: Aug 25 2026 10:00)
---
Nice! I've added this task:
[E][ ] interview (from: Aug 25 2026 09:00 to: Aug 25 2026 09:30)
---
Nice! I've added this task:
[E][ ] later meeting (from: Aug 30 2026 10:00 to: Aug 30 2026 11:00)
---
Nice! I've added this task:
[D][ ] draft (by: Aug 26 2026 23:59)
---
Nice! I've added this task:
[D][ ] slides (by: Aug 26 2026 23:59)
---
Nice! I've added this task:
[D][ ] final report (by: Aug 30 2026 23:59)
---
Most urgent event:
- [E][ ] demo (from: Aug 25 2026 09:00 to: Aug 25 2026 10:00)
- [E][ ] interview (from: Aug 25 2026 09:00 to: Aug 25 2026 09:30)
Most urgent deadline:
- [D][ ] draft (by: Aug 26 2026 23:59)
- [D][ ] slides (by: Aug 26 2026 23:59)
---
Nice seeing you. Until next time!
```

## Test Case: Reject urgent arguments

Aim: Verify that `urgent` rejects extra arguments with the expected usage message.

```input
urgent now
bye
```

```expected
Error: `urgent` does not take extra text.
Try: urgent
---
Nice seeing you. Until next time!
```

## Test Case: Mark a task as done

Aim: Verify that `mark 1` marks the first task as done.

```input
todo borrow book
mark 1
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
I've marked this task as done:
[T][X] borrow book
---
Nice seeing you. Until next time!
```

## Test Case: Unmark a task as not done

Aim: Verify that `unmark 1` changes a previously marked task back to not done.

```input
todo borrow book
mark 1
unmark 1
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
I've marked this task as done:
[T][X] borrow book
---
I've marked this task as not done:
[T][ ] borrow book
---
Nice seeing you. Until next time!
```

## Test Case: Reject invalid mark index

Aim: Verify that `mark` reports an error when the task number is outside the list.

```input
todo borrow book
mark 2
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
Error: That task number is not in the list.
Try: mark TASK_NUMBER
---
Nice seeing you. Until next time!
```

## Test Case: Reject non-numeric task number

Aim: Verify that a task-number command rejects a non-numeric task number without a package prefix in its message.

```input
mark one
bye
```

```expected
Error: A task number must be a whole number.
Try: mark TASK_NUMBER
---
Nice seeing you. Until next time!
```

## Test Case: Reject malformed deadline

Aim: Verify that a `deadline` command without `/by` shows the expected usage message.

```input
deadline return book
bye
```

```expected
Error: I could not find a due time.
Try: deadline DESCRIPTION /by dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject malformed event

Aim: Verify that an `event` command without `/from` and `/to` shows the expected usage message.

```input
event project meeting
bye
```

```expected
Error: I could not find both event times.
Try: event DESCRIPTION /from dd-MM-yyyy HHmm /to dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject invalid deadline date-time

Aim: Verify that a `deadline` command rejects a due time that does not match the required date-time format.

```input
deadline return book /by Sunday
bye
```

```expected
Error: The date and time is invalid.
Try: dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject invalid event date-times

Aim: Verify that an `event` command rejects start and end times that do not match the required date-time format.

```input
event project meeting /from Mon /to 4pm
bye
```

```expected
Error: The date and time is invalid.
Try: dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty todo description

Aim: Verify that a `todo` command with no description is rejected.

```input
todo
bye
```

```expected
Error: A to-do needs a description.
Try: todo DESCRIPTION
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty deadline description

Aim: Verify that a `deadline` command with no description is rejected.

```input
deadline /by 25-08-2026 1900
bye
```

```expected
Error: A deadline needs a description and due time.
Try: deadline DESCRIPTION /by dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty event description

Aim: Verify that an `event` command with no description is rejected.

```input
event /from 25-08-2026 1400 /to 25-08-2026 1600
bye
```

```expected
Error: An event needs a description.
Try: event DESCRIPTION /from dd-MM-yyyy HHmm /to dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty event start time

Aim: Verify that an `event` command with no start time shows the usage message.

```input
event main game /from /to 25-08-2026 1900
bye
```

```expected
Error: An event needs both a start time and an end time.
Try: event DESCRIPTION /from dd-MM-yyyy HHmm /to dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty event end time

Aim: Verify that an `event` command with no end time shows the same usage message.

```input
event main game /from 25-08-2026 1900 /to
bye
```

```expected
Error: An event needs both a start time and an end time.
Try: event DESCRIPTION /from dd-MM-yyyy HHmm /to dd-MM-yyyy HHmm
---
Nice seeing you. Until next time!
```
