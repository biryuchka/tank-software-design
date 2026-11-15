package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {

    @Test
    void standsStillUntilItIsSentOn() {
        Bullet bullet = new Bullet(new GridPoint2(2, 2), Direction.UP);

        assertFalse(bullet.isMoving());
        assertEquals(new GridPoint2(2, 2), bullet.getCoordinates());
    }

    @Test
    void knowsTheTileAheadOfIt() {
        assertEquals(new GridPoint2(2, 3), new Bullet(new GridPoint2(2, 2), Direction.UP).getNextCoordinates());
        assertEquals(new GridPoint2(1, 2), new Bullet(new GridPoint2(2, 2), Direction.LEFT).getNextCoordinates());
    }

    @Test
    void fliesOnInTheDirectionItWasFired() {
        Bullet bullet = new Bullet(new GridPoint2(2, 2), Direction.RIGHT);

        bullet.flyOn();

        assertTrue(bullet.isMoving());
        assertEquals(new GridPoint2(3, 2), bullet.getDestinationCoordinates());
    }

    @Test
    void arrivesAfterEnoughTimeHasPassed() {
        Bullet bullet = new Bullet(new GridPoint2(2, 2), Direction.RIGHT);
        bullet.flyOn();

        bullet.update(1f);

        assertFalse(bullet.isMoving());
        assertEquals(new GridPoint2(3, 2), bullet.getCoordinates());
    }

    @Test
    void takesNoOrders() {
        Bullet bullet = new Bullet(new GridPoint2(2, 2), Direction.RIGHT);

        assertFalse(bullet instanceof Movable);
        assertTrue(bullet instanceof Moving);
    }

    @Test
    void neverSurvivesAHit() {
        assertTrue(new Bullet(new GridPoint2(2, 2), Direction.UP).hit(1));
    }
}
