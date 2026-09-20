package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private Tank tank;
    private Level level;

    @BeforeEach
    void setUp() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        level = new Level(10, 8, tank, asList(new Tree(new GridPoint2(1, 3)), new Tree(new GridPoint2(4, 5))));
    }

    @Test
    void emptyTileIsFree() {
        assertTrue(level.isFree(new GridPoint2(5, 5)));
    }

    @Test
    void tileWithTreeIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(1, 3)));
        assertFalse(level.isFree(new GridPoint2(4, 5)));
    }

    @Test
    void tileOutsideLevelIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(-1, 4)));
        assertFalse(level.isFree(new GridPoint2(10, 4)));
        assertFalse(level.isFree(new GridPoint2(4, -1)));
        assertFalse(level.isFree(new GridPoint2(4, 8)));
    }

    @Test
    void cornersOfLevelAreFree() {
        assertTrue(level.isFree(new GridPoint2(0, 0)));
        assertTrue(level.isFree(new GridPoint2(9, 7)));
    }

    @Test
    void anyObstacleBlocksItsTile() {
        GridPoint2 rockCoordinates = new GridPoint2(6, 6);
        Obstacle rock = () -> rockCoordinates;
        Level levelWithRock = new Level(10, 8, tank, asList(rock));

        assertFalse(levelWithRock.isFree(rockCoordinates));
        assertTrue(levelWithRock.isFree(new GridPoint2(6, 7)));
    }

    @Test
    void updateMovesTank() {
        tank.move(Direction.UP, level);

        level.update(1f);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }
}
