import java.util.List;

/**
 * Shows all tasks currently stored in the task list.
 */
public class ListCommand extends Command {
    @Override
    public void execute(List<Task> tasks, Ui ui) {
        ui.showTaskList(tasks);
    }
}
