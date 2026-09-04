package nico;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adds a deadline task to the task list.
 */
public class DeadlineCommand extends ParamCommand {
    /**
     * Creates a command with the deadline details supplied by the user.
     */
    public DeadlineCommand(String details) {
        super("deadline", details);
    }

    /**
     * Validates the supplied details, adds a deadline task, and saves it.
     *
     * @param tasks tasks currently managed by the chatbot
     * @param ui user interface used to display confirmation
     * @throws NicoException if the description or due time is invalid
     */
    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        String details = getParameter();
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
