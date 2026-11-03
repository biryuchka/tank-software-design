package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private Tank tank;
    private Level level;

    @BeforeEach
    void setUp() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        level = new Level(10, 8);
        level.addPlayer(tank);
        level.addObstacle(new Tree(new GridPoint2(1, 3)));
        level.addObstacle(new Tree(new GridPoint2(4, 5)));
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
    void updateMovesEveryMovable() {
        Tank second = new Tank(new GridPoint2(6, 6), Direction.RIGHT);
        level.addCombatant(second);
        tank.move(Direction.UP, level);
        second.move(Direction.UP, level);

        level.update(1f);

        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
        assertEquals(new GridPoint2(6, 7), second.getCoordinates());
    }

    @Test
    void tileUnderATankIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void movingTankHoldsBothTilesItIsBetween() {
        tank.move(Direction.UP, level);

        // it has not arrived yet, so it holds the tile it left and the tile it heads to
        assertTrue(tank.isMoving());
        assertFalse(level.isFree(new GridPoint2(1, 1)));
        assertFalse(level.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void anotherTankCannotEnterTileBeingLeft() {
        Tank chaser = new Tank(new GridPoint2(0, 1), Direction.RIGHT);
        level.addCombatant(chaser);
        tank.move(Direction.UP, level);

        chaser.move(Direction.RIGHT, level);

        assertFalse(chaser.isMoving());
        assertEquals(new GridPoint2(0, 1), chaser.getCoordinates());
    }

    @Test
    void anotherTankCannotEnterTileBeingEntered() {
        Tank rival = new Tank(new GridPoint2(2, 2), Direction.RIGHT);
        level.addCombatant(rival);
        tank.move(Direction.UP, level);

        rival.move(Direction.LEFT, level);

        assertFalse(rival.isMoving());
        assertEquals(new GridPoint2(2, 2), rival.getCoordinates());
    }

    @Test
    void tileIsFreeAgainAfterTheTankHasLeftIt() {
        tank.move(Direction.UP, level);
        level.update(1f);

        assertTrue(level.isFree(new GridPoint2(1, 1)));
        assertFalse(level.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void twoTanksCannotTakeTheSameTile() {
        Tank first = new Tank(new GridPoint2(5, 5), Direction.RIGHT);
        Tank second = new Tank(new GridPoint2(7, 5), Direction.RIGHT);
        level.addCombatant(first);
        level.addCombatant(second);

        first.move(Direction.RIGHT, level);
        second.move(Direction.LEFT, level);

        assertTrue(first.isMoving());
        assertFalse(second.isMoving());
    }

    @Test
    void remembersPlayerAmongMovables() {
        assertSame(tank, level.getPlayer());
    }

    @Test
    void keepsEveryObjectForRendering() {
        assertEquals(3, level.getObjects().size());
    }

    @Test
    void objectsCannotBeAddedBypassingLevel() {
        assertThrows(UnsupportedOperationException.class,
                () -> level.getObjects().add(new Tree(new GridPoint2(0, 7))));
    }
}
