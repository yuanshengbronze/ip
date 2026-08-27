package nico;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {
    @Test
    void newTask_isIncompleteAndFormattedAsIncomplete() {
        Task task = new Task("read book");

        assertFalse(task.isDone());
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    void markAsDone_incompleteTask_taskBecomesComplete() {
        Task task = new Task("read book");

        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("[X] read book", task.toString());
    }

    @Test
    void unmarkAsDone_completedTask_taskBecomesIncomplete() {
        Task task = new Task("read book");
        task.markAsDone();

        task.unmarkAsDone();

        assertFalse(task.isDone());
        assertEquals("[ ] read book", task.toString());
    }
}
