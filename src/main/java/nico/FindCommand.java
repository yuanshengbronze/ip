package nico;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Finds tasks whose descriptions contain a given keyword. */
public class FindCommand extends ParamCommand {
    /**
     * Creates a command that finds tasks with the given keyword.
     *
     * @param keyword Keyword to search for in task descriptions.
     */
    public FindCommand(String keyword) {
        super("find", keyword);
    }

    @Override
    public void execute(List<Task> tasks, Ui ui) throws NicoException {
        String keyword = getParameter();
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new NicoException("\tPlease use: find KEYWORD");
        }

        List<Task> taskList = findKeywordMatches(tasks, keyword.trim());
        ui.showTaskGroup("Here are the matching tasks in your list", taskList);
    }

    /**
     * Returns tasks whose descriptions contain the keyword, regardless of letter case.
     */
    private List<Task> findKeywordMatches(List<Task> tasks, String keyword) {
        List<Task> result = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                result.add(task);
            }
        }
        return result;
    }
}
