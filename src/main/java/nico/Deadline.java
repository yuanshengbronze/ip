package nico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specific date and time.
 */
public class Deadline extends Task {
    protected LocalDateTime dueTime;

    /**
     * Creates a deadline task with its due date and time.
     *
     * @param description Task description.
     * @param dueTime Date and time the task is due.
     */
    public Deadline(String description, LocalDateTime dueTime) {
        super(description);
        this.dueTime = dueTime;
    }

    /**
     * Returns the deadline's due date and time for sorting and comparison.
     */
    public LocalDateTime getDueTime() {
        return dueTime;
    }

    /**
     * Returns this deadline in the format used for display and storage.
     *
     * @return Formatted deadline text, including its completion status and due time.
     */
    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        String dueTimeString = dueTime.format(DateTimeFormatter.ofPattern(Nico.DATE_TIME_OUTPUT_FORMAT));
        return String.format("[D]%s %s (by: %s)", status, description, dueTimeString);
    }
}
