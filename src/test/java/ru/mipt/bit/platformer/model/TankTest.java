package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static class CollectingRegistry implements BulletRegistry {

        private final List<Bullet> bullets = new ArrayList<>();

        @Override
        public void addBullet(Bullet bullet) {
            bullets.add(bullet);
        }
    }

    private static final float LONG_ENOUGH_TO_ARRIVE = 1f;
    private static final Field EVERYTHING_IS_FREE = coordinates -> true;
    private static final Field NOTHING_IS_FREE = coordinates -> false;

    private Tank tank;

    @BeforeEach
    void setUp() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
    }

    @Test
    void standsStillWhenCreated() {
        assertFalse(tank.isMoving());
        assertEquals(tank.getCoordinates(), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void startsMovingToFreeTile() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        // the tank is still in the old tile until it arrives
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void arrivesToDestinationAfterUpdate() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.update(LONG_ENOUGH_TO_ARRIVE);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void turnsButDoesNotMoveWhenTileIsTaken() {
        tank.move(Direction.LEFT, NOTHING_IS_FREE);
        tank.update(LONG_ENOUGH_TO_ARRIVE);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation());
    }

    @Test
    void ignoresNewDirectionWhileMoving() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.move(Direction.LEFT, EVERYTHING_IS_FREE);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void movesAgainAfterItHasArrived() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);
        tank.update(LONG_ENOUGH_TO_ARRIVE);

        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 3), tank.getDestinationCoordinates());
    }

    @Test
    void movesWithGivenSpeed() {
        Tank slowTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 1f);
        Tank fastTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.5f);

        slowTank.move(Direction.UP, EVERYTHING_IS_FREE);
        fastTank.move(Direction.UP, EVERYTHING_IS_FREE);
        slowTank.update(0.5f);
        fastTank.update(0.5f);

        assertTrue(slowTank.isMoving());
        assertFalse(fastTank.isMoving());
    }

    @Test
    void losesHealthWhenHitAndLeavesTheLevelOnlyWhenItGoesNegative() {
        Tank target = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f, 1, 0.5f);

        assertFalse(target.hit(1));
        assertEquals(0, target.getHealth());
        assertTrue(target.hit(1));
    }

    @Test
    void firesABulletOnTheTileItLooksAt() {
        Tank shooter = new Tank(new GridPoint2(3, 3), Direction.RIGHT);
        CollectingRegistry registry = new CollectingRegistry();

        shooter.shoot(registry);

        assertEquals(1, registry.bullets.size());
        assertEquals(new GridPoint2(4, 3), registry.bullets.get(0).getCoordinates());
    }

    @Test
    void firesWhereItLooksAfterTurning() {
        Tank shooter = new Tank(new GridPoint2(3, 3), Direction.RIGHT);
        CollectingRegistry registry = new CollectingRegistry();

        shooter.move(Direction.UP, NOTHING_IS_FREE);
        shooter.shoot(registry);

        assertEquals(new GridPoint2(3, 4), registry.bullets.get(0).getCoordinates());
    }

    @Test
    void cannotFireAgainUntilItHasReloaded() {
        Tank shooter = new Tank(new GridPoint2(3, 3), Direction.RIGHT, 0.4f, 3, 0.5f);
        CollectingRegistry registry = new CollectingRegistry();

        shooter.shoot(registry);
        shooter.shoot(registry);

        assertEquals(1, registry.bullets.size());
        assertFalse(shooter.canShoot());
    }

    @Test
    void firesAgainOnceReloaded() {
        Tank shooter = new Tank(new GridPoint2(3, 3), Direction.RIGHT, 0.4f, 3, 0.5f);
        CollectingRegistry registry = new CollectingRegistry();

        shooter.shoot(registry);
        shooter.update(1f);
        shooter.shoot(registry);

        assertEquals(2, registry.bullets.size());
    }

    @Test
    void reportsProgressWhileMoving() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.update(0.1f);

        assertTrue(tank.getMovementProgress() > 0f);
        assertTrue(tank.getMovementProgress() < 1f);
        assertTrue(tank.isMoving());
    }
}
