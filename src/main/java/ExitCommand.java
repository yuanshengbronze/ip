import java.util.List;

/**
 * Exits the chatbot after showing the farewell message.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(List<Task> tasks, Ui ui) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
