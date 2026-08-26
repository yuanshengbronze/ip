package nico;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adds a deadline task to the task list.
 */
public class DeadlineCommand extends Command {
    private final String details;

    /**
     * Creates a command with the deadline details supplied by the user.
     */
    public DeadlineCommand(String details) {
        this.details = details;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        if (details == null) {
            throw new NicoException("\tDescription can't be empty. Please use: deadline DESCRIPTION /by DUE TIME");
        }
        String[] parts = details.split("\\s*/by\\s+", 2);
        if (parts.length < 2) {
            throw new NicoException("\tPlease use: deadline DESCRIPTION /by DUE TIME");
        }
        if (parts[0].trim().isEmpty()) {
            throw new NicoException("\tDescription can't be empty. Please use: deadline DESCRIPTION /by DUE TIME");
        }
        if (parts[1].trim().isEmpty()) {
            throw new NicoException("\tDue time can't be empty. Please use: deadline DESCRIPTION /by DUE TIME");
        }

        LocalDateTime dueTime = Parser.parseDateTime(parts[1].trim(), Nico.DATE_TIME_INPUT_FORMAT);
        Deadline newDeadline = new Deadline(parts[0].trim(), dueTime);
        tasks.add(newDeadline);
        TaskStorage.writeTask(Nico.FILE_PATH, newDeadline);
        ui.showLine();
        ui.showTaskAdded(newDeadline, tasks.size());
    }
}
