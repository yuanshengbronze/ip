# UI Test Plan

The `test-ui` skill uses this file as the source of truth for console UI tests.
Each test case records its aim, the console inputs, and the expected output after each input.

```program
java -cp "%REPO_ROOT%\out\production\ip" nico.Nico
```

```build
javac -d out/production/ip src/main/java/*.java
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
Please use: find KEYWORD
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
Sorry, I don't understand what that command means :(
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
Please use: urgent
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
Sorry, that task number is not in the list.
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
Task number must be an integer.
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
Please use: deadline DESCRIPTION /by DUE TIME
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
Please use: event DESCRIPTION /from START TIME /to END TIME
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
Please use dd-MM-yyyy HHmm format for date and time!
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
Please use dd-MM-yyyy HHmm format for date and time!
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
Description can't be empty. Please use: todo DESCRIPTION
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
Description can't be empty. Please use: deadline DESCRIPTION /by DUE TIME
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
The description cannot be empty.
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
Please use: event DESCRIPTION /from START TIME /to END TIME
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
Please use: event DESCRIPTION /from START TIME /to END TIME
---
Nice seeing you. Until next time!
```
