package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Movable;

public class MoveCommand implements Command {

    private final Movable movable;
    private final Direction direction;
    private final Field field;

    public MoveCommand(Movable movable, Direction direction, Field field) {
        this.movable = movable;
        this.direction = direction;
        this.field = field;
    }

    @Override
    public void execute() {
        movable.move(direction, field);
    }
}
