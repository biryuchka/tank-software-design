package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;

public class MoveCommand implements Command {

    private final Movable movable;
    private final Direction direction;
    private final Level level;

    public MoveCommand(Movable movable, Direction direction, Level level) {
        this.movable = movable;
        this.direction = direction;
        this.level = level;
    }

    @Override
    public void execute() {
        movable.move(direction, level);
    }
}
