package nico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that happens between a start date-time and an end date-time.
 */
public class Event extends Task {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    /**
     * Creates an event task with its description, start time, and end time.
     *
     * @param description text describing the event
     * @param startTime date and time at which the event starts
     * @param endTime date and time at which the event ends
     */
    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the event's start date and time for sorting and comparison.
     *
     * @return this event's start date and time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Returns this event in the format used for display and storage.
     *
     * @return formatted event text, including its completion status and time range
     */
    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        String startTimeString = startTime.format(DateTimeFormatter.ofPattern(Nico.DATE_TIME_OUTPUT_FORMAT));
        String endTimeString = endTime.format(DateTimeFormatter.ofPattern(Nico.DATE_TIME_OUTPUT_FORMAT));
        return String.format("[E]%s %s (from: %s to: %s)", status, description, startTimeString, endTimeString);
    }
}
