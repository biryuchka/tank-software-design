package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleCommandTest {

    private static class Lamp implements Switch {

        private boolean on;

        @Override
        public void toggle() {
            on = !on;
        }
    }

    @Test
    void turnsTheSwitchOn() {
        Lamp lamp = new Lamp();

        new ToggleCommand(lamp).execute();

        assertTrue(lamp.on);
    }

    @Test
    void turnsItBackOffOnTheNextExecution() {
        Lamp lamp = new Lamp();
        Command command = new ToggleCommand(lamp);

        command.execute();
        command.execute();

        assertFalse(lamp.on);
    }
}
