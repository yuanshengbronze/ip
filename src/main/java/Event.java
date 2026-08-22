import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        String startTimeString = startTime.format(DateTimeFormatter.ofPattern(Nico.DATE_TIME_OUTPUT_FORMAT));
        String endTimeString = endTime.format(DateTimeFormatter.ofPattern(Nico.DATE_TIME_OUTPUT_FORMAT));
        return String.format("[E]%s %s (from: %s to: %s)", status, description, startTimeString, endTimeString);
    }
}
