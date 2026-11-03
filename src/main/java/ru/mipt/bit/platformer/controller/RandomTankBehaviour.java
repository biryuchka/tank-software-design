package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Movable;

import java.util.Random;

/** The simplest behaviour there is: every frame the tank is told to go in a random direction. */
public class RandomTankBehaviour implements TankBehaviour {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;

    public RandomTankBehaviour() {
        this(new Random());
    }

    public RandomTankBehaviour(Random random) {
        this.random = random;
    }

    @Override
    public Command decide(Movable tank, Field field) {
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return new MoveCommand(tank, direction, field);
    }
}
