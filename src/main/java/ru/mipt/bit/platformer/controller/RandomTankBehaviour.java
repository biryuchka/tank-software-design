package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.BulletRegistry;
import ru.mipt.bit.platformer.model.Combatant;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;

import java.util.Random;

/**
 * The simplest behaviour there is: now and then the tank fires, otherwise it is told
 * to drive in a random direction.
 */
public class RandomTankBehaviour implements TankBehaviour {

    public static final float DEFAULT_SHOOTING_CHANCE = 0.15f;

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;
    private final float shootingChance;

    public RandomTankBehaviour() {
        this(new Random(), DEFAULT_SHOOTING_CHANCE);
    }

    public RandomTankBehaviour(Random random) {
        this(random, DEFAULT_SHOOTING_CHANCE);
    }

    public RandomTankBehaviour(Random random, float shootingChance) {
        this.random = random;
        this.shootingChance = shootingChance;
    }

    @Override
    public Command decide(Combatant tank, Field field, BulletRegistry registry) {
        if (random.nextFloat() < shootingChance) {
            return new ShootCommand(tank, registry);
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return new MoveCommand(tank, direction, field);
    }
}
