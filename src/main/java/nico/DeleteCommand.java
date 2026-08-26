package nico;

import java.util.List;

/**
 * Removes one task from the task list.
 */
public class DeleteCommand extends TaskNumberCommand {
    /**
     * Creates a command to delete the specified task number.
     */
    public DeleteCommand(String taskNumberText) {
        super("delete", taskNumberText);
    }

    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        Task task = getTask(tasks, ui);
        tasks.remove(task);
        TaskStorage.writeAllTasks(Nico.FILE_PATH, tasks);
        ui.showTaskRemoved(task, tasks.size());
    }
}
