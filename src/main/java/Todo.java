public class Todo extends Task{
    protected String startTime;
    protected String endTime;
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return "[T]" + status + " " + this.description;
    }
}
