import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime dueTime;
    public Deadline(String description, LocalDateTime dueTime) {
        super(description);
        this.dueTime = dueTime;
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        String dueTimeString = dueTime.format(DateTimeFormatter.ofPattern(Nico.DATE_TIME_OUTPUT_FORMAT));
        return String.format("[D]%s %s (by: %s)", status, description, dueTimeString);
    }
}
