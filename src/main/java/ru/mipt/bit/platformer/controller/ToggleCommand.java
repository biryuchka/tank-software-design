package ru.mipt.bit.platformer.controller;

public class ToggleCommand implements Command {

    private final Switch target;

    public ToggleCommand(Switch target) {
        this.target = target;
    }

    @Override
    public void execute() {
        target.toggle();
    }
}
