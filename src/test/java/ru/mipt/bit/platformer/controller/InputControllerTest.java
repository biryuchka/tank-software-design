package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputControllerTest {

    private static final int FIRST_KEY = 1;
    private static final int SECOND_KEY = 2;
    private static final int UNBOUND_KEY = 3;

    private final FakeKeyboard keyboard = new FakeKeyboard();
    private final InputController controller = new InputController(keyboard);

    @Test
    void runsCommandOfPressedKey() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        keyboard.press(FIRST_KEY);
        controller.processInput();

        assertEquals(1, command.executions);
    }

    @Test
    void doesNothingWhenNoKeyIsPressed() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        controller.processInput();

        assertEquals(0, command.executions);
    }

    @Test
    void runsEveryPressedKeyCommand() {
        CountingCommand first = new CountingCommand();
        CountingCommand second = new CountingCommand();
        controller.registerCommand(FIRST_KEY, first);
        controller.registerCommand(SECOND_KEY, second);

        keyboard.press(FIRST_KEY);
        keyboard.press(SECOND_KEY);
        controller.processInput();

        assertEquals(1, first.executions);
        assertEquals(1, second.executions);
    }

    @Test
    void ignoresPressedKeyWithoutCommand() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        keyboard.press(UNBOUND_KEY);
        controller.processInput();

        assertEquals(0, command.executions);
    }

    @Test
    void severalKeysMayRunTheSameCommand() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);
        controller.registerCommand(SECOND_KEY, command);

        keyboard.press(FIRST_KEY);
        keyboard.press(SECOND_KEY);
        controller.processInput();

        assertEquals(2, command.executions);
    }

    @Test
    void runsCommandOnEveryProcessedFrame() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        keyboard.press(FIRST_KEY);
        controller.processInput();
        controller.processInput();

        assertEquals(2, command.executions);
    }

    private static class FakeKeyboard implements Keyboard {

        private final Set<Integer> pressedKeys = new HashSet<>();

        void press(int key) {
            pressedKeys.add(key);
        }

        @Override
        public boolean isKeyPressed(int key) {
            return pressedKeys.contains(key);
        }
    }

    private static class CountingCommand implements Command {

        private int executions;

        @Override
        public void execute() {
            executions++;
        }
    }
}
