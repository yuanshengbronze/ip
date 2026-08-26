import java.util.List;

/**
 * Marks one task as incomplete.
 */
public class UnmarkCommand extends TaskNumberCommand {
    /**
     * Creates a command to unmark the specified task number.
     */
    public UnmarkCommand(String taskNumberText) {
        super("unmark", taskNumberText);
    }

    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        Task task = getTask(tasks, ui);
        task.unmarkAsDone();
        TaskStorage.writeAllTasks(Nico.FILE_PATH, tasks);
        ui.showTaskMarkedNotDone(task);
    }
}
