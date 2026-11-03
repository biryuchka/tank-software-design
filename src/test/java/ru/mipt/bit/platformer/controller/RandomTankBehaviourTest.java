package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankBehaviourTest {

    private static final Field EVERYTHING_IS_FREE = coordinates -> true;

    @Test
    void alwaysProducesACommand() {
        TankBehaviour behaviour = new RandomTankBehaviour(new Random(1L));
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);

        assertNotNull(behaviour.decide(tank, EVERYTHING_IS_FREE));
    }

    @Test
    void itsCommandActuallyMovesTheTank() {
        TankBehaviour behaviour = new RandomTankBehaviour(new Random(1L));
        Tank tank = new Tank(new GridPoint2(4, 4), Direction.RIGHT);

        behaviour.decide(tank, EVERYTHING_IS_FREE).execute();

        assertTrue(tank.isMoving());
    }

    @Test
    void soonerOrLaterEveryDirectionIsChosen() {
        TankBehaviour behaviour = new RandomTankBehaviour(new Random(7L));
        Set<Float> rotations = new HashSet<>();

        for (int i = 0; i < 200; i++) {
            Tank tank = new Tank(new GridPoint2(4, 4), Direction.RIGHT);
            behaviour.decide(tank, EVERYTHING_IS_FREE).execute();
            rotations.add(tank.getRotation());
        }

        assertEquals(Direction.values().length, rotations.size());
    }

    @Test
    void sameSeedGivesSameDecisions() {
        Tank first = new Tank(new GridPoint2(4, 4), Direction.RIGHT);
        Tank second = new Tank(new GridPoint2(4, 4), Direction.RIGHT);

        new RandomTankBehaviour(new Random(3L)).decide(first, EVERYTHING_IS_FREE).execute();
        new RandomTankBehaviour(new Random(3L)).decide(second, EVERYTHING_IS_FREE).execute();

        assertEquals(first.getDestinationCoordinates(), second.getDestinationCoordinates());
    }
}
