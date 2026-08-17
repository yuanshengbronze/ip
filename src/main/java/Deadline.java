public class Deadline extends Task {
    protected String dueTime;
    public Deadline(String description, String dueTime) {
        super(description);
        this.dueTime = dueTime;
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return String.format("[D]%s %s (by: %s)", status, description, dueTime);
    }
}
