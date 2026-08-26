import java.util.List;

/**
 * Marks one task as complete.
 */
public class MarkCommand extends TaskNumberCommand {
    /**
     * Creates a command to mark the specified task number.
     */
    public MarkCommand(String taskNumberText) {
        super("mark", taskNumberText);
    }

    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        Task task = getTask(tasks, ui);
        task.markAsDone();
        TaskStorage.writeAllTasks(Nico.FILE_PATH, tasks);
        ui.showTaskMarkedDone(task);
    }
}
