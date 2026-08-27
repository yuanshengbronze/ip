package nico;

/** Represents a task without a date or time. */
public class Todo extends Task {
    /**
     * Creates a todo task with the given description.
     *
     * @param description Task description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return "[T]" + status + " " + this.description;
    }
}
