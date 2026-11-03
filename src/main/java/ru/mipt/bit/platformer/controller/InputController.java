package ru.mipt.bit.platformer.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/** Turns the keys pressed right now into commands. */
public class InputController implements CommandProvider {

    private final Keyboard keyboard;
    private final Map<Integer, Command> commands = new LinkedHashMap<>();

    public InputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void registerCommand(int key, Command command) {
        commands.put(key, command);
    }

    @Override
    public Collection<Command> commands() {
        Collection<Command> pressed = new ArrayList<>();
        for (Map.Entry<Integer, Command> keyCommand : commands.entrySet()) {
            if (keyboard.isKeyPressed(keyCommand.getKey())) {
                pressed.add(keyCommand.getValue());
            }
        }
        return pressed;
    }
}
