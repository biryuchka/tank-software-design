package ru.mipt.bit.platformer.controller;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Runs the command bound to every key which is pressed right now.
 * A new key binding (shooting on space, for example) does not require changing this class.
 */
public class InputController {

    private final Keyboard keyboard;
    private final Map<Integer, Command> commands = new LinkedHashMap<>();

    public InputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void registerCommand(int key, Command command) {
        commands.put(key, command);
    }

    public void processInput() {
        for (Map.Entry<Integer, Command> keyCommand : commands.entrySet()) {
            if (keyboard.isKeyPressed(keyCommand.getKey())) {
                keyCommand.getValue().execute();
            }
        }
    }
}
