package ru.mipt.bit.platformer.controller;

import java.util.LinkedHashMap;
import java.util.Map;

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
