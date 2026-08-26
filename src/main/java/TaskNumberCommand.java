import java.util.List;

/**
 * Provides common validation for commands that identify a task by its list number.
 */
public abstract class TaskNumberCommand extends Command {
    private final String taskNumberText;
    private final String commandWord;

    /**
     * Creates a command that operates on a task number.
     */
    protected TaskNumberCommand(String commandWord, String taskNumberText) {
        this.commandWord = commandWord;
        this.taskNumberText = taskNumberText;
    }

    /**
     * Returns the requested task after validating its one-based list number.
     */
    protected Task getTask(List<Task> tasks, Ui ui) throws NicoException {
        if (taskNumberText == null) {
            throw new NicoException("\tNo task number. Please use: " + commandWord + " TASK_NUMBER");
        }
        try {
            int taskNumber = Integer.parseInt(taskNumberText.trim());
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showLine();
                throw new NicoException("\tSorry, that task number is not in the list.");
            }
            return tasks.get(taskNumber - 1);
        } catch (NumberFormatException e) {
            throw new NicoException("\tTask number must be an integer.");
        }
    }
}
