package nico;

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
    /**
     * Validates the supplied description, adds a todo task, and saves it.
     *
     * @param tasks tasks currently managed by the chatbot
     * @param ui user interface used to display confirmation
     * @throws NicoException if the description is empty or the task cannot be saved
     */
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
