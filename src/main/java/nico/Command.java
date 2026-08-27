package nico;

import java.util.List;

/**
 * Represents a user command that can be executed by the chatbot.
 */
public abstract class Command {
    /**
     * Executes this command using the current task list and UI.
     */
    public abstract void execute(List<Task> tasks, Ui ui) throws NicoException;
    /**
     * Returns whether this command should exit the chatbot.
     */
    public boolean isExit() {
        return false;
    }
}
