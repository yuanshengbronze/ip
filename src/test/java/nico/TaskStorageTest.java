package nico;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskStorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void createTasksFile_nestedMissingPath_fileAndParentDirectoriesCreated() throws IOException {
        Path filePath = temporaryDirectory.resolve("nested/tasks.txt");

        TaskStorage.createTasksFile(filePath);

        assertTrue(Files.isRegularFile(filePath));
    }

    @Test
    void writeTask_multipleTasks_tasksAppendedInOrder() throws IOException, NicoException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");

        TaskStorage.writeTask(filePath, new Todo("read book"));
        TaskStorage.writeTask(filePath, new Todo("return book"));

        assertEquals(List.of("[T][ ] read book", "[T][ ] return book"), Files.readAllLines(filePath));
    }

    @Test
    void writeAllTasks_existingTasks_fileReplacedWithFormattedTasks() throws IOException, NicoException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        TaskStorage.writeTask(filePath, new Todo("old task"));
        Todo completedTodo = new Todo("read book");
        completedTodo.markAsDone();
        Deadline deadline = new Deadline("return book", LocalDateTime.of(2026, 8, 25, 19, 0));

        TaskStorage.writeAllTasks(filePath, List.of(completedTodo, deadline));

        assertEquals(List.of(
                "[T][X] read book",
                "[D][ ] return book (by: Aug 25 2026 19:00)"), Files.readAllLines(filePath));
    }

    @Test
    void writeAllTasks_emptyList_existingFileEmptied() throws IOException, NicoException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        TaskStorage.writeTask(filePath, new Todo("read book"));

        TaskStorage.writeAllTasks(filePath, List.of());

        assertEquals(List.of(), Files.readAllLines(filePath));
    }

    @Test
    void writeTask_directoryPath_nicoExceptionThrown() {
        NicoException exception = assertThrows(NicoException.class,
                () -> TaskStorage.writeTask(temporaryDirectory, new Todo("read book")));

        assertEquals("Sorry, I could not save this task to tasks.txt.", exception.getMessage());
    }
}
