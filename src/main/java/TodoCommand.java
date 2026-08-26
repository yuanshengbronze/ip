import java.util.List;

/**
 * Adds a todo task to the task list.
 */
public class TodoCommand extends Command {
    private final String description;

    /**
     * Creates a command with the todo description supplied by the user.
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        if (description == null || description.trim().isEmpty()) {
            throw new NicoException("\tDescription can't be empty. Please use: todo DESCRIPTION");
        }
        Task newTodo = new Todo(description.trim());
        tasks.add(newTodo);
        TaskStorage.writeTask(Nico.FILE_PATH, newTodo);
        ui.showLine();
        ui.showTaskAdded(newTodo, tasks.size());
    }
}
