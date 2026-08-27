package nico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Interprets user input and creates command objects when possible.
 */
public class Parser {
    /**
     * Splits user input into a command word and its optional argument.
     *
     * @param fullCommand complete command text entered by the user
     * @return command object representing the entered command
     * @throws NicoException if the command is unknown or has invalid arguments
     */
    public static Command parseCommand(String fullCommand) throws NicoException {
        String[] commandArray = fullCommand.trim().split("\\s+", 2);
        String argument = commandArray.length > 1 ? commandArray[1] : null;
        switch (commandArray[0]) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand();
            case "urgent":
                if (argument != null) {
                    throw new NicoException("\tPlease use: urgent");
                }
                return new UrgentCommand();
            case "mark":
                return new MarkCommand(argument);
            case "unmark":
                return new UnmarkCommand(argument);
            case "todo":
                return new TodoCommand(argument);
            case "deadline":
                return new DeadlineCommand(argument);
            case "event":
                return new EventCommand(argument);
            case "delete":
                return new DeleteCommand(argument);
            default:
                throw new NicoException("\tSorry, I don't understand what that command means :(");
            }
    }

    /**
     * Converts user-entered date-time text to a LocalDateTime value.
     *
     * @param input date-time text to parse
     * @param format date-time pattern the input must follow
     * @return parsed date-time value
     * @throws NicoException if the input does not match the requested format
     */
    public static LocalDateTime parseDateTime(String input, String format) throws NicoException {
        try {
            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            throw new NicoException(String.format("\tPlease use %s format for date and time!", format));
        }
    }
}
