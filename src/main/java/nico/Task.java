package nico;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void unmarkAsDone() {
        this.isDone = false;
    }

    /**
     * Returns whether this task has been marked as completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task in the format used for display and storage.
     *
     * @return formatted task text, including its completion status
     */
    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return status + " " + this.description;
    }
}
