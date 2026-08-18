# UI Test Plan

The `test-ui` skill uses this file as the source of truth for console UI tests.
Each test case records its aim, the console inputs, and the expected output after each input.

```program
java -cp out/production/ip Nico
```

```build
javac -d out/production/ip src/main/java/Task.java src/main/java/Todo.java src/main/java/Deadline.java src/main/java/Event.java src/main/java/Nico.java
```

## Test Case: Reject unknown command

Aim: Verify that an unrecognized command is rejected instead of being added as a task.

```input
return book
bye
```

```expected
Sorry, I don't know what that command means.
---
Nice seeing you. Until next time!
```

## Test Case: Add a todo task

Aim: Verify that a `todo` command creates a Todo task formatted with `[T]`.

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

Aim: Verify that a `deadline` command parses the description and integer `/by` value.

```input
deadline return book /by 7
bye
```

```expected
Nice! I've added this task:
[D][ ] return book (by: 7)
---
Nice seeing you. Until next time!
```

## Test Case: Add an event task

Aim: Verify that an `event` command parses the description, integer `/from` value, and integer `/to` value.

```input
event project meeting /from 14 /to 16
bye
```

```expected
Nice! I've added this task:
[E][ ] project meeting (from: 14 to: 16)
---
Nice seeing you. Until next time!
```

## Test Case: List mixed task types

Aim: Verify that `list` displays Todo, Deadline, and Event tasks with their indices.

```input
todo borrow book
deadline submit report /by 23
event project meeting /from 14 /to 16
list
bye
```

```expected
Nice! I've added this task:
[T][ ] borrow book
---
Nice! I've added this task:
[D][ ] submit report (by: 23)
---
Nice! I've added this task:
[E][ ] project meeting (from: 14 to: 16)
---
1. [T][ ] borrow book
2. [D][ ] submit report (by: 23)
3. [E][ ] project meeting (from: 14 to: 16)
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

## Test Case: Reject malformed deadline

Aim: Verify that a `deadline` command without `/by` shows the expected usage message.

```input
deadline return book
bye
```

```expected
Please use: deadline DESCRIPTION /by WHEN
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
Please use: event DESCRIPTION /from WHEN /to WHEN
---
Nice seeing you. Until next time!
```

## Test Case: Reject non-integer deadline time

Aim: Verify that a `deadline` command rejects a non-integer due time.

```input
deadline return book /by Sunday
bye
```

```expected
The due time must be an integer.
---
Nice seeing you. Until next time!
```

## Test Case: Reject non-integer event times

Aim: Verify that an `event` command rejects non-integer start and end times.

```input
event project meeting /from Mon /to 4pm
bye
```

```expected
The start and end times must be integers.
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
The description cannot be empty.
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty deadline description

Aim: Verify that a `deadline` command with no description is rejected.

```input
deadline /by 7
bye
```

```expected
The description cannot be empty.
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty event description

Aim: Verify that an `event` command with no description is rejected.

```input
event /from 14 /to 16
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
event main game /from /to 7pm
bye
```

```expected
Please use: event DESCRIPTION /from WHEN /to WHEN
---
Nice seeing you. Until next time!
```

## Test Case: Reject empty event end time

Aim: Verify that an `event` command with no end time shows the same usage message.

```input
event main game /from 7pm /to
bye
```

```expected
Please use: event DESCRIPTION /from WHEN /to WHEN
---
Nice seeing you. Until next time!
```
