package nico;

/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task{
    protected String startTime;
    protected String endTime;
    /**
     * Creates a todo task with the given description.
     *
     * @param description text describing the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo in the format used for display and storage.
     *
     * @return formatted todo text, including its completion status
     */
    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return "[T]" + status + " " + this.description;
    }
}
