package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;

import java.util.Collection;
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
        execute(controller.commands());

        assertEquals(1, command.executions);
    }

    @Test
    void doesNothingWhenNoKeyIsPressed() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        execute(controller.commands());

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
        execute(controller.commands());

        assertEquals(1, first.executions);
        assertEquals(1, second.executions);
    }

    @Test
    void ignoresPressedKeyWithoutCommand() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        keyboard.press(UNBOUND_KEY);
        execute(controller.commands());

        assertEquals(0, command.executions);
    }

    @Test
    void severalKeysMayRunTheSameCommand() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);
        controller.registerCommand(SECOND_KEY, command);

        keyboard.press(FIRST_KEY);
        keyboard.press(SECOND_KEY);
        execute(controller.commands());

        assertEquals(2, command.executions);
    }

    @Test
    void runsCommandOnEveryProcessedFrame() {
        CountingCommand command = new CountingCommand();
        controller.registerCommand(FIRST_KEY, command);

        keyboard.press(FIRST_KEY);
        execute(controller.commands());
        execute(controller.commands());

        assertEquals(2, command.executions);
    }

    @Test
    void runsOnPressCommandOnlyOnceWhileTheKeyIsHeld() {
        CountingCommand command = new CountingCommand();
        controller.registerOnPress(FIRST_KEY, command);

        keyboard.press(FIRST_KEY);
        execute(controller.commands());
        keyboard.nextFrame();
        execute(controller.commands());

        assertEquals(1, command.executions);
    }

    @Test
    void runsOnPressCommandAgainAfterTheKeyWasReleased() {
        CountingCommand command = new CountingCommand();
        controller.registerOnPress(FIRST_KEY, command);

        keyboard.press(FIRST_KEY);
        execute(controller.commands());
        keyboard.nextFrame();
        keyboard.release(FIRST_KEY);
        keyboard.press(FIRST_KEY);
        execute(controller.commands());

        assertEquals(2, command.executions);
    }

    @Test
    void heldAndPressedCommandsLiveSideBySide() {
        CountingCommand held = new CountingCommand();
        CountingCommand pressed = new CountingCommand();
        controller.registerCommand(FIRST_KEY, held);
        controller.registerOnPress(SECOND_KEY, pressed);

        keyboard.press(FIRST_KEY);
        keyboard.press(SECOND_KEY);
        execute(controller.commands());
        keyboard.nextFrame();
        execute(controller.commands());

        assertEquals(2, held.executions);
        assertEquals(1, pressed.executions);
    }

    private static void execute(Collection<Command> commands) {
        for (Command command : commands) {
            command.execute();
        }
    }

    private static class FakeKeyboard implements Keyboard {

        private final Set<Integer> pressedKeys = new HashSet<>();
        private final Set<Integer> justPressedKeys = new HashSet<>();

        void press(int key) {
            pressedKeys.add(key);
            justPressedKeys.add(key);
        }

        void release(int key) {
            pressedKeys.remove(key);
            justPressedKeys.remove(key);
        }

        // the next frame: the keys are still held, but none of them is new any more
        void nextFrame() {
            justPressedKeys.clear();
        }

        @Override
        public boolean isKeyPressed(int key) {
            return pressedKeys.contains(key);
        }

        @Override
        public boolean isKeyJustPressed(int key) {
            return justPressedKeys.contains(key);
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
