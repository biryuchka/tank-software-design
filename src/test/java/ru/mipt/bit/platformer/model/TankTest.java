package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float LONG_ENOUGH_TO_ARRIVE = 1f;

    private Tank tank;
    private Level level;

    @BeforeEach
    void setUp() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        level = new Level(10, 8, tank, asList(new Tree(new GridPoint2(2, 1))));
    }

    @Test
    void standsStillWhenCreated() {
        assertFalse(tank.isMoving());
        assertEquals(tank.getCoordinates(), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void startsMovingToFreeTile() {
        tank.move(Direction.UP, level);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        // the tank is still in the old tile until it arrives
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void arrivesToDestinationAfterUpdate() {
        tank.move(Direction.UP, level);

        tank.update(LONG_ENOUGH_TO_ARRIVE);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void turnsButDoesNotMoveIntoObstacle() {
        tank.move(Direction.RIGHT, level);
        tank.update(LONG_ENOUGH_TO_ARRIVE);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void turnsButDoesNotMoveOutsideLevel() {
        Tank cornerTank = new Tank(new GridPoint2(0, 0), Direction.UP);
        Level emptyLevel = new Level(10, 8, cornerTank, emptyList());

        cornerTank.move(Direction.LEFT, emptyLevel);
        cornerTank.update(LONG_ENOUGH_TO_ARRIVE);

        assertEquals(new GridPoint2(0, 0), cornerTank.getCoordinates());
        assertEquals(Direction.LEFT.getRotation(), cornerTank.getRotation());
    }

    @Test
    void ignoresNewDirectionWhileMoving() {
        tank.move(Direction.UP, level);

        tank.move(Direction.LEFT, level);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void movesAgainAfterItHasArrived() {
        tank.move(Direction.UP, level);
        tank.update(LONG_ENOUGH_TO_ARRIVE);

        tank.move(Direction.UP, level);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 3), tank.getDestinationCoordinates());
    }

    @Test
    void movesWithGivenSpeed() {
        Tank slowTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 1f);
        Tank fastTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.5f);
        Level emptyLevel = new Level(10, 8, slowTank, emptyList());

        slowTank.move(Direction.UP, emptyLevel);
        fastTank.move(Direction.UP, emptyLevel);
        slowTank.update(0.5f);
        fastTank.update(0.5f);

        assertTrue(slowTank.isMoving());
        assertFalse(fastTank.isMoving());
    }

    @Test
    void reportsProgressWhileMoving() {
        tank.move(Direction.UP, level);

        tank.update(0.1f);

        assertTrue(tank.getMovementProgress() > 0f);
        assertTrue(tank.getMovementProgress() < 1f);
        assertTrue(tank.isMoving());
    }
}
