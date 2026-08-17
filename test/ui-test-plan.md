# UI Test Plan

The `test-ui` skill uses this file as the source of truth for console UI tests.
Each test case records its aim, the console inputs, and the expected output after each input.

```program
java -cp out/production/ip Nico
```

```build
javac -d out/production/ip src/main/java/Task.java src/main/java/Todo.java src/main/java/Deadline.java src/main/java/Event.java src/main/java/Nico.java
```

## Test Case: Add a plain task

Aim: Verify that an unrecognized input is added as a plain task using the current confirmation format.

```input
return book
bye
```

```expected
Nice! I've added this task:
[ ] return book
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

Aim: Verify that a `deadline` command parses the description and `/by` value.

```input
deadline return book /by Sunday
bye
```

```expected
Nice! I've added this task:
[D] [ ] return book (by: Sunday)
---
Nice seeing you. Until next time!
```

## Test Case: Add an event task

Aim: Verify that an `event` command parses the description, `/from` value, and `/to` value.

```input
event project meeting /from Mon 2pm /to 4pm
bye
```

```expected
Nice! I've added this task:
[E] [ ] project meeting (from: Mon 2pm to: 4pm)
---
Nice seeing you. Until next time!
```

## Test Case: List mixed task types

Aim: Verify that `list` displays plain, Todo, Deadline, and Event tasks with their indices.

```input
return book
todo borrow book
deadline submit report /by Friday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

```expected
Nice! I've added this task:
[ ] return book
---
Nice! I've added this task:
[T][ ] borrow book
---
Nice! I've added this task:
[D] [ ] submit report (by: Friday)
---
Nice! I've added this task:
[E] [ ] project meeting (from: Mon 2pm to: 4pm)
---
1. [ ] return book
2. [T][ ] borrow book
3. [D] [ ] submit report (by: Friday)
4. [E] [ ] project meeting (from: Mon 2pm to: 4pm)
---
Nice seeing you. Until next time!
```

## Test Case: Mark a task as done

Aim: Verify that `mark 1` marks the first task as done.

```input
return book
mark 1
bye
```

```expected
Nice! I've added this task:
[ ] return book
---
I've marked this task as done:
[X] return book
---
Nice seeing you. Until next time!
```

## Test Case: Unmark a task as not done

Aim: Verify that `unmark 1` changes a previously marked task back to not done.

```input
return book
mark 1
unmark 1
bye
```

```expected
Nice! I've added this task:
[ ] return book
---
I've marked this task as done:
[X] return book
---
I've marked this task as not done:
[ ] return book
---
Nice seeing you. Until next time!
```

## Test Case: Reject invalid mark index

Aim: Verify that `mark` reports an error when the task number is outside the list.

```input
return book
mark 2
bye
```

```expected
Nice! I've added this task:
[ ] return book
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
