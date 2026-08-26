package nico;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmarkAsDone() {
        this.isDone = false;
    }

    /**
     * Returns whether this task has been marked as completed.
     */
    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return status + " " + this.description;
    }
}
