package nico;

import java.util.List;

/**
 * Exits the chatbot after showing the farewell message.
 */
public class ExitCommand extends Command {
    @Override
    /**
     * Displays the chatbot farewell message.
     *
     * @param tasks tasks currently managed by the chatbot; not changed by this command
     * @param ui user interface used to display the farewell
     */
    public void execute(List<Task> tasks, Ui ui) {
        ui.showGoodbye();
    }

    @Override
    /**
     * Indicates that the chatbot should end after this command.
     *
     * @return {@code true}
     */
    public boolean isExit() {
        return true;
    }
}
