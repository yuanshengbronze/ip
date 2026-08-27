package nico;

import java.util.List;

/**
 * Provides common validation for commands that identify a task by its list number.
 */
public abstract class TaskNumberCommand extends ParamCommand {
    /**
     * Creates a command that operates on a task number.
     */
    protected TaskNumberCommand(String commandWord, String taskNumberText) {
        super(commandWord, taskNumberText);
    }

    /**
     * Returns the requested task after validating its one-based list number.
     */
    protected Task getTask(List<Task> tasks, Ui ui) throws NicoException {
        String taskNumberText = getParameter();
        if (taskNumberText == null || taskNumberText.trim().isEmpty()) {
            throw new NicoException("\tNo task number. Please use: " + getCommandWord() + " TASK_NUMBER");
        }
        try {
            int taskNumber = Integer.parseInt(taskNumberText.trim());
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showLine();
                throw new NicoException("\tSorry, that task number is not in the list.");
            }
            return tasks.get(taskNumber - 1);
        } catch (NumberFormatException e) {
            throw new NicoException("\tnico.Task number must be an integer.");
        }
    }
}
