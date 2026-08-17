public class Event extends Task {
    protected String startTime;
    protected String endTime;
    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return String.format("[E]%s %s (from: %s to: %s)", status, description, startTime, endTime);
    }
}
