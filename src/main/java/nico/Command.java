package nico;

import java.util.List;

/**
 * Represents a user command that can be executed by the chatbot.
 */
public abstract class Command {
    /**
     * Executes this command using the current task list and UI.
     *
     * @param tasks tasks currently managed by the chatbot
     * @param ui user interface used to display command output
     * @throws NicoException if the command input is invalid or cannot be processed
     */
    public abstract void execute(List<Task> tasks, Ui ui) throws NicoException;

    /**
     * Returns whether this command should exit the chatbot.
     *
     * @return {@code true} if the chatbot should end after this command
     */
    public boolean isExit() {
        return false;
    }
}
