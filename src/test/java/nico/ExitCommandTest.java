package nico;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExitCommandTest {
    @Test
    void isExit_exitCommand_trueReturned() {
        assertTrue(new ExitCommand().isExit());
    }
}
